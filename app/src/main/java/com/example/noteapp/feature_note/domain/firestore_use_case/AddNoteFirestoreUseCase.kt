package com.example.noteapp.feature_note.domain.firestore_use_case

import com.example.noteapp.feature_note.domain.model.FireStoreNote
import com.example.noteapp.feature_note.domain.repository.FireStoreRepository

class AddNoteFirestoreUseCase(private val repository: FireStoreRepository) {
    suspend operator fun invoke(fireStoreNote: FireStoreNote) {
        repository.addNote(fireStoreNote)
    }
}