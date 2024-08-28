package com.example.noteapp.presentation.viewmodels.uitests

import NoteAppTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.noteapp.presentation.MainActivity
import com.example.noteapp.presentation.notes.components.NotesScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NotesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun whenNotesScreenIsDisplayed_thenAddNoteButtonIsDisplayed() {
        composeTestRule.setContent {
            NoteAppTheme {
                NotesScreen(navController = rememberNavController(), viewModel = hiltViewModel())
            }
        }
        composeTestRule.onNodeWithContentDescription("Add note").isNotDisplayed()
    }
}