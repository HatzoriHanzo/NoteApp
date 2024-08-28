package com.example.noteapp.feature_note.domain.model

data class FireStoreNote(
    val id: Int? = null,
    val userId: String = "",
    val title: String,
    val content: String,
    val reminderDate: Long,
    val token: String
)
