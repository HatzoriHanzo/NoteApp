package com.example.noteapp.presentation.add_edit_note.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ReminderReceiver.mediaPlayer?.stop()
        ReminderReceiver.mediaPlayer?.release()
        ReminderReceiver.mediaPlayer = null
    }
}