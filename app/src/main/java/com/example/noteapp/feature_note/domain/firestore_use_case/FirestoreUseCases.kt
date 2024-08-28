package com.example.noteapp.feature_note.domain.firestore_use_case

import com.example.noteapp.feature_note.domain.model.FireStoreNote
import com.example.noteapp.feature_note.domain.repository.FireStoreRepository
import com.example.noteapp.feature_note.domain.util.SharedPrefManager
import javax.inject.Inject

class FirestoreUseCases @Inject constructor(
    private val repository: FireStoreRepository, private val sharedPrefManager: SharedPrefManager
) {

    suspend fun addNote(note: FireStoreNote) {
        repository.addNote(note)
    }

    suspend fun getNote(noteId: Int): FireStoreNote? {
        return repository.getNote(noteId)
    }

    suspend fun updateNote(note: FireStoreNote) {
        repository.updateNote(note)
    }

    suspend fun deleteNote(noteId: Int) {
        repository.deleteNote(noteId)
    }

    fun saveUserId(userId: String) {
        sharedPrefManager.saveUserId(userId)
    }

    fun getUserId(): String? {
        return sharedPrefManager.getUserId()
    }
}