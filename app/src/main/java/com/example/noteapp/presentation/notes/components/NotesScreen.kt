package com.example.noteapp.presentation.notes.components

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.example.noteapp.R
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.presentation.notes.NotesEvent
import com.example.noteapp.presentation.notes.NotesViewModel
import com.example.noteapp.presentation.util.Screen
import com.example.noteapp.ui.components.LargeSpacer
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val color1 = Note.noteColors.random()
    val color2 = Note.noteColors.random()
    val backgroundColor = remember { Animatable(color1) }

    LaunchedEffect(key1 = true) {
        backgroundColor.animateTo(
            targetValue = color2, animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2500), repeatMode = RepeatMode.Reverse
            )
        )
    }
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.extra_large_margin)),
                onClick = {
                    navController.navigate(Screen.AddEditNoteScreen.route)
                },
                backgroundColor = Note.noteColors.random(),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
        scaffoldState = scaffoldState,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor.value.copy(alpha = 0.9f))
                .padding(dimensionResource(id = R.dimen.large_margin))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.large_margin)),
                    text = stringResource(R.string.suas_anotacoes),
                    style = MaterialTheme.typography.h4
                )
                IconButton(onClick = { viewModel.onEvent(NotesEvent.ToggleOrderSection) }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "Add note")
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    })
            }
            LargeSpacer()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes) { note ->
                    NoteItem(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                Screen.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
                            )
                        }, note = note, onDeleteClick = {
                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                        scope.launch {
                            val result = scaffoldState.snackbarHostState.showSnackbar(
                                message = "Nota deletada.", actionLabel = "Desfazer"
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.onEvent(NotesEvent.RestoreNote)
                            }
                        }
                    })
                    LargeSpacer()
                }

            }
        }
    }


}

