package com.example.noteapp.feature_note.domain.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

    fun saveFirebaseUserId(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.apply()
    }

    fun getFirebaseUserId(): String? {
        return sharedPreferences.getString("userId", null)
    }

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }
    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }
}
