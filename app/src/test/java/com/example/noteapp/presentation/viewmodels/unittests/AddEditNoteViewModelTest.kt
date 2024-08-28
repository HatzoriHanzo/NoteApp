package com.example.noteapp.presentation.viewmodels.unittests

import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import com.example.noteapp.feature_note.domain.firestore_use_case.FirestoreUseCases
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import com.example.noteapp.presentation.add_edit_note.AddEditNoteEvent
import com.example.noteapp.presentation.add_edit_note.AddEditNoteViewModel
import com.example.noteapp.presentation.viewmodels.BaseViewModelTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddEditNoteViewModelTest : BaseViewModelTest() {
    @Mock
    private lateinit var noteUseCases: NoteUseCases

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    @Mock
    private lateinit var firestoreUseCases: FirestoreUseCases

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var viewModel: AddEditNoteViewModel

    val testDispatcher = TestCoroutineDispatcher()


    @Before
    fun setUp() = runBlocking {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)
        viewModel = AddEditNoteViewModel(
            noteUseCases,
            savedStateHandle,
            firestoreUseCases,
            sharedPreferences
        )

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun `when setReminder is called, then _noteReminder value is updated`() = runBlocking {
        val reminderTime = System.currentTimeMillis()

        viewModel.setReminder(reminderTime)

        assertEquals(reminderTime, viewModel.noteReminder.value)
    }

    @Test
    fun `when SetReminder event is called, then noteReminder value is updated`() = runBlocking {
        val reminderTime = System.currentTimeMillis()
        val event = AddEditNoteEvent.SetReminder(reminderTime)

        viewModel.onEvent(event)

        assertEquals(reminderTime, viewModel.noteReminder.value)
    }

    @Test
    fun `when EnteredTitle event is called, then noteTitle value is updated`() = runBlocking {
        val title = "Test Title"
        val event = AddEditNoteEvent.EnteredTitle(title)

        viewModel.onEvent(event)

        assertEquals(title, viewModel.noteTitle.value.text)
    }

    @Test
    fun `when EnteredContent event is called, then noteContent value is updated`() = runBlocking {
        val content = "Test Content"
        val event = AddEditNoteEvent.EnteredContent(content)

        viewModel.onEvent(event)

        assertEquals(content, viewModel.noteContent.value.text)
    }

    @Test
    fun `when ChangeColor event is called, then noteColor value is updated`() = runBlocking {
        val color = Color.Red.toArgb()
        val event = AddEditNoteEvent.ChangeColor(color)

        viewModel.onEvent(event)

        assertEquals(color, viewModel.noteColor.value)
    }
}