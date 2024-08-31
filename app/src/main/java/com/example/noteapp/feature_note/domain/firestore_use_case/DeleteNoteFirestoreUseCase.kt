package com.example.noteapp.feature_note.domain.firestore_use_case

import com.example.noteapp.feature_note.domain.repository.FireStoreRepository

class DeleteNoteFirestoreUseCase(private val repository: FireStoreRepository) {
    suspend operator fun invoke(fireStoreNoteId: Int) {
        repository.deleteNote(fireStoreNoteId)
    }
}