package com.example.noteapp.presentation.add_edit_note.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import com.example.noteapp.R
import com.example.noteapp.feature_note.domain.model.Note

@Composable
fun showPermissionDialog(title: String, message: String, openDialog: MutableState<Boolean>) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                Button(onClick = {
                    // Handle the logic when the user clicks "Allow"
                    openDialog.value = false
                }) {
                    Text("Allow")
                }
            },
            dismissButton = {
                Button(onClick = {
                    // Handle the logic when the user clicks "Deny"
                    openDialog.value = false
                }) {
                    Text("Deny")
                }
            }
        )
    }
}