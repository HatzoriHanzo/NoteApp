package com.example.noteapp.presentation.add_edit_note.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.noteapp.presentation.add_edit_note.AddEditNoteViewModel
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ReminderPicker(
    showPicker: MutableState<Boolean>, viewModel: AddEditNoteViewModel
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            showTimePicker(context, calendar, viewModel)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    if (showPicker.value) {
        Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { datePickerDialog.show() }) {
                Text("Definir hora do lembrete")
            }
        }

    }
}

private fun showTimePicker(
    context: Context, calendar: Calendar, viewModel: AddEditNoteViewModel
) {
    val timePickerDialog = TimePickerDialog(
        context, { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            val reminderTime = calendar.timeInMillis
            viewModel.setReminder(reminderTime)
            viewModel.setAlarm(
                context, reminderTime
            ) // Call setAlarm after setting the reminder time
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )
    timePickerDialog.show()
}
