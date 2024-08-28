package com.example.noteapp.presentation.add_edit_note.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.noteapp.R
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.presentation.add_edit_note.AddEditNoteEvent
import com.example.noteapp.presentation.add_edit_note.AddEditNoteViewModel
import com.example.noteapp.presentation.util.formatToddMMyyyyHHmm
import com.example.noteapp.presentation.util.toLocalDateTime
import com.example.noteapp.ui.components.LargeSpacer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AddEditNoteScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val snackbarHostState = remember { SnackbarHostState() }

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                    )
                }

                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.large_margin)),
            onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
            backgroundColor = Note.noteColors.random()
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = stringResource(R.string.salvar_nota)
            )

        }
    }, snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(top = dimensionResource(id = R.dimen.not_so_extra_large_margin))
                .padding(dimensionResource(id = R.dimen.large_margin))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(modifier = Modifier
                        .size(50.dp)
                        .shadow(15.dp, CircleShape)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = 3.dp, color = if (viewModel.noteColor.value == colorInt) {
                                Color.Black
                            } else Color.Transparent, shape = CircleShape
                        )
                        .clickable {
                            scope.launch {
                                noteBackgroundAnimatable.animateTo(
                                    targetValue = Color(colorInt), animationSpec = tween(
                                        durationMillis = 500
                                    )
                                )
                            }
                            viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                        })
                }

            }
            LargeSpacer()

            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                isHintVisible = titleState.isHintVisible,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )
            LargeSpacer()
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                isHintVisible = contentState.isHintVisible,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.height(200.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (viewModel.noteReminder.value != null) {
                    viewModel.noteReminder.value?.let { reminderValue ->
                        val reminderDateTime = reminderValue.toLocalDateTime()
                        val formattedReminderDateTime = reminderDateTime.formatToddMMyyyyHHmm()
                        Text(
                            stringResource(R.string.editar_data_do_lembrete),
                            color = MaterialTheme.colors.onSurface,
                            style = MaterialTheme.typography.subtitle1
                        )
                        Text(
                            text = formattedReminderDateTime,
                            color = MaterialTheme.colors.onSurface,
                            style = MaterialTheme.typography.button
                        )
                        Switch(checked = viewModel.noteReminderSwitch.value,
                            onCheckedChange = { isChecked ->
                                viewModel.onEvent(AddEditNoteEvent.ChangeReminderSwitch(isChecked))
                            })
                    }
                } else {
                    Text(
                        text = stringResource(R.string.definir_lembrete),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.h6
                    )
                    Switch(checked = viewModel.noteReminderSwitch.value,
                        onCheckedChange = { isChecked ->
                            viewModel.onEvent(AddEditNoteEvent.ChangeReminderSwitch(isChecked))
                        })
                }
            }
            if (viewModel.noteReminderSwitch.value) {
                ReminderPicker(
                    viewModel = viewModel, showPicker = viewModel.showDatePicker
                )
            }

        }


    }
}