package com.example.noteapp.feature_note.domain.firestore_use_case

data class FirestoreUseCases (
    val addNote: AddNoteFirestoreUseCase,
    val deleteNote: DeleteNoteFirestoreUseCase,
    val saveFirebaseUserId: SaveFirebaseUserIdUseCase,
    val getFirebaseUserId: GetFirebaseUserIdUseCase
)