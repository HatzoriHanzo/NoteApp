package com.example.noteapp.feature_note.domain.firestore_use_case

import com.example.noteapp.feature_note.domain.repository.FireStoreRepository

class GetFirebaseUserIdUseCase(private val repository: FireStoreRepository) {
     operator fun invoke(): String? = repository.getFirebaseUserId()
}