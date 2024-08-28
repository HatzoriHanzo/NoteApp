package com.example.noteapp.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState
import java.time.LocalDateTime

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    data class ChangeReminderSwitch(val isChecked: Boolean) : AddEditNoteEvent()
    data class SetReminder(val reminder: Long) : AddEditNoteEvent()
    object ShowDatePicker : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()

}
