package com.example.noteapp.feature_note.data.repository

import com.example.noteapp.feature_note.domain.model.FireStoreNote
import com.example.noteapp.feature_note.domain.repository.FireStoreRepository
import com.example.noteapp.feature_note.domain.util.SharedPrefManager
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore, private val sharedPrefManager: SharedPrefManager
) : FireStoreRepository {

    override suspend fun addNote(note: FireStoreNote) {
        firestore.collection("notes").document(note.id.toString()).set(note).await()
    }

    override suspend fun deleteNote(noteId: Int) {
        firestore.collection("notes").document(noteId.toString()).delete().await()
    }

    override fun saveFirebaseUserId(userId: String) {
        sharedPrefManager.saveFirebaseUserId(userId)
    }

    override fun getFirebaseUserId(): String? = sharedPrefManager.getFirebaseUserId()
}
