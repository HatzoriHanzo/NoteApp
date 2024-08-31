package com.example.noteapp.feature_note.domain.repository

import com.example.noteapp.feature_note.domain.model.FireStoreNote

interface FireStoreRepository {
    suspend fun addNote(note: FireStoreNote)
    suspend fun deleteNote(noteId: Int)
    fun saveFirebaseUserId(userId: String)
    fun getFirebaseUserId(): String?
}