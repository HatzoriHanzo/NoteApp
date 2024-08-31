package com.example.noteapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.noteapp.feature_note.domain.util.SharedPrefManager
import com.example.noteapp.presentation.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: NotificationManager


    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it.body)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        sharedPrefManager.saveToken(token)
    }


    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder =
            NotificationCompat.Builder(this, "channel_id").setContentTitle("Note App - Firebase")
                .setContentText(messageBody).setSmallIcon(R.drawable.ic_firebase_notification)
                .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }
}

