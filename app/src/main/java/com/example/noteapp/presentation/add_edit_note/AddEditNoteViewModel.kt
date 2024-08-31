package com.example.noteapp.presentation.add_edit_note

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.firestore_use_case.FirestoreUseCases
import com.example.noteapp.feature_note.domain.model.FireStoreNote
import com.example.noteapp.feature_note.domain.model.InvalidNoteException
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import com.example.noteapp.feature_note.domain.util.SharedPrefManager
import com.example.noteapp.presentation.add_edit_note.components.ReminderReceiver
import com.example.noteapp.presentation.util.toEpochMilli
import com.example.noteapp.presentation.util.toLocalDateTime
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,
    private val firestoreUseCases: FirestoreUseCases,
    private val sharedPreferences: SharedPrefManager
) : ViewModel() {
    init {
        getNewToken()
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.let { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title, isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content, isHintVisible = false
                        )
                        _noteColor.intValue = note.color

                        _noteReminder.value = note.reminderTime
                    }
                }
            } else {
                currentNoteId = generateUniqueIntId()
            }
        }
    }


    private val _noteReminderSwitch = mutableStateOf(false)
    val noteReminderSwitch: MutableState<Boolean> get() = _noteReminderSwitch

    private val _showDatePicker = mutableStateOf(false)
    val showDatePicker: MutableState<Boolean> get() = _showDatePicker

    private val _noteReminder = mutableStateOf<Long?>(null) // Change this to a nullable type
    val noteReminder: MutableState<Long?> get() = _noteReminder


    private var currentNoteId: Int? = null
    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Digite o título da nota:"))
    val noteTitle: MutableState<NoteTextFieldState>
        get() = _noteTitle

    private val _noteContent =
        mutableStateOf(NoteTextFieldState(hint = "Escreva sua nota aqui! :)"))
    val noteContent: MutableState<NoteTextFieldState>
        get() = _noteContent

    private val _noteColor = mutableIntStateOf(Note.noteColors.random().toArgb())
    val noteColor: MutableState<Int>
        get() = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }

    private fun getToken(): String? {
        return sharedPreferences.getToken()
    }

    private fun generateUniqueIntId(): Int {
        return (System.currentTimeMillis() and 0xfffffff).toInt()
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.SetReminder -> {
                val reminderDateTime = event.reminder.toLocalDateTime()
                val reminder = reminderDateTime.toEpochMilli()
                noteReminder.value = reminder
            }

            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ShowDatePicker -> {
                _showDatePicker.value = true
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.intValue = event.color
            }

            is AddEditNoteEvent.ChangeReminderSwitch -> {
                noteReminderSwitch.value = event.isChecked
                showDatePicker.value = event.isChecked
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId,
                                reminderTime = noteReminder.value
                            )
                        )
                        firestoreUseCases.addNote(
                            FireStoreNote(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                id = currentNoteId,
                                reminderDate = noteReminder.value ?: 0,
                                token = getToken() ?: "",
                                userId = firestoreUseCases.getFirebaseUserId() ?: ""
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Não foi possível salvar a nota!"))
                    }
                }
            }
        }
    }

    fun setReminder(reminderTime: Long) {
        _noteReminder.value = reminderTime
    }


    private fun getNewToken() {
        val existingToken = sharedPreferences.getToken()
        if (existingToken == null) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val newToken = task.result

                sharedPreferences.saveToken(newToken)
            }
        }
    }

    fun setAlarm(context: Context, reminderTime: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("note_content", noteContent.value.text)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent
            )
        }
    }

}