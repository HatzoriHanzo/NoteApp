package com.example.noteapp.presentation.main

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.noteapp.feature_note.domain.util.SharedPrefManager
import com.example.noteapp.presentation.util.Constants.REQUEST_CODE
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    fun signIn() {
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInAnonymously:success")
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        sharedPrefManager.saveFirebaseUserId(it.uid)
                    }
                } else {
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkAndRequestPermissions(context: Context) {
        val permissions = arrayOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.SCHEDULE_EXACT_ALARM
        )

        val notGrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (notGrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as Activity, notGrantedPermissions, REQUEST_CODE)
        }
    }
}
