package com.example.noteapp.presentation.add_edit_note.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.noteapp.R
import com.example.noteapp.presentation.util.Constants.REMINDER_CHANNEL_ID

class ReminderReceiver : BroadcastReceiver() {
    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        val noteContent = intent.getStringExtra("note_content")
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(context, alarmSound)
        mediaPlayer?.start()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "ReminderChannel"
        val descriptionText = "Channel for Note reminder"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(REMINDER_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_local_notification)
            .setContentTitle(context.getString(R.string.note_app))
            .setContentText(context.getString(R.string.lembrete_da_nota, noteContent ?: ""))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDeleteIntent(getDismissIntent(context))
            .build()

        notificationManager.notify(1, notification)
    }

    private fun getDismissIntent(context: Context): PendingIntent {
        val intent = Intent(context, DismissReceiver::class.java)
        return PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}


