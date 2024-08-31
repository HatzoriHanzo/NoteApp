package com.example.noteapp.presentation.main

import NoteAppTheme
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.presentation.add_edit_note.components.AddEditNoteScreen
import com.example.noteapp.presentation.notes.components.NotesScreen
import com.example.noteapp.presentation.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController, startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(navArgument("noteId") {
                                type = NavType.IntType; defaultValue = -1
                            }, navArgument("noteColor") {
                                type = NavType.IntType; defaultValue = -1
                            })
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(navController = navController, noteColor = color)
                        }
                    }

                    // Observe the ViewModel for any events like sign-in success/failure or permission handling
                    viewModel.signIn()
                    viewModel.checkAndRequestPermissions(this)
                }
            }
        }
    }
}



