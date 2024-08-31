package com.example.noteapp.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.noteapp.feature_note.data.data_source.NoteDatabase
import com.example.noteapp.feature_note.data.repository.FirestoreRepositoryImpl
import com.example.noteapp.feature_note.data.repository.NoteRepositoryImpl
import com.example.noteapp.feature_note.domain.firestore_use_case.AddNoteFirestoreUseCase
import com.example.noteapp.feature_note.domain.firestore_use_case.DeleteNoteFirestoreUseCase
import com.example.noteapp.feature_note.domain.firestore_use_case.FirestoreUseCases
import com.example.noteapp.feature_note.domain.firestore_use_case.GetFirebaseUserIdUseCase
import com.example.noteapp.feature_note.domain.firestore_use_case.SaveFirebaseUserIdUseCase
import com.example.noteapp.feature_note.domain.repository.FireStoreRepository
import com.example.noteapp.feature_note.domain.repository.NoteRepository
import com.example.noteapp.feature_note.domain.use_case.AddNoteUseCase
import com.example.noteapp.feature_note.domain.use_case.DeleteNoteUseCase
import com.example.noteapp.feature_note.domain.use_case.GetNoteUseCase
import com.example.noteapp.feature_note.domain.use_case.GetNotesUseCase
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import com.example.noteapp.feature_note.domain.util.SharedPrefManager
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(app, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideFirestoreUseCases(
        repository: FireStoreRepository
    ): FirestoreUseCases {
        return FirestoreUseCases(
            addNote = AddNoteFirestoreUseCase(repository),
            deleteNote = DeleteNoteFirestoreUseCase(repository),
            saveFirebaseUserId = SaveFirebaseUserIdUseCase(repository),
            getFirebaseUserId = GetFirebaseUserIdUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun proviteNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideFireStoreRepository(
        firestore: FirebaseFirestore, sharedPrefManager: SharedPrefManager
    ): FireStoreRepository {
        return FirestoreRepositoryImpl(firestore, sharedPrefManager)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPrefManager(@ApplicationContext context: Context): SharedPrefManager {
        return SharedPrefManager(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotesUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            addNote = AddNoteUseCase(repository),
            getNote = GetNoteUseCase(repository)
        )
    }

    @Singleton
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}