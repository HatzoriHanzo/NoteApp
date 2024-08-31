package com.example.noteapp.presentation.notes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.firestore_use_case.FirestoreUseCases
import com.example.noteapp.feature_note.domain.model.FireStoreNote
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import com.example.noteapp.feature_note.domain.util.NoteOrder
import com.example.noteapp.feature_note.domain.util.OrderType
import com.example.noteapp.feature_note.domain.util.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val firestoreUseCases: FirestoreUseCases,
    private val sharedPrefManager: SharedPrefManager

) : ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: MutableState<NotesState> = _state
    var recentlyDeletedNote: Note? = null
    var recentlyDeletedFireStoreNote: FireStoreNote? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            firestoreUseCases.saveFirebaseUserId(userId)
        }
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class && state.value.noteOrder.orderType == event.noteOrder.orderType) {
                    return
                }
                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                    firestoreUseCases.deleteNote(event.note.id ?: 0)
                    recentlyDeletedFireStoreNote = FireStoreNote(
                        id = event.note.id,
                        userId = firestoreUseCases.getFirebaseUserId()  ?: "",
                        title = event.note.title,
                        content = event.note.content,
                        reminderDate = event.note.reminderTime ?: 0,
                        token = sharedPrefManager.getToken() ?: ""
                    )
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    firestoreUseCases.addNote(recentlyDeletedFireStoreNote ?: return@launch)
                    recentlyDeletedNote = null
                    recentlyDeletedFireStoreNote = null
                }
            }

        }
    }

    fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder).onEach { notes ->
            _state.value = state.value.copy(notes = notes, noteOrder = noteOrder)
        }.launchIn(viewModelScope)
    }

}


