package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun EditInstructionDialog(
    currentInstruction: String, onDismissRequest: () -> Unit, onConfirm: (String) -> Unit
) {
    var instructionInputText by remember { mutableStateOf(currentInstruction) }
    val focusRequester = remember { FocusRequester() }

    AlertDialog(
        properties = DialogProperties(),

        modifier = Modifier.wrapContentHeight(),
        onDismissRequest = onDismissRequest,
        title = null,
        text = {
            Box(
                Modifier

            ) {
                OutlinedTextField(

                    value = instructionInputText,
                    onValueChange = { instructionInputText = it },
                    label = { Text("Инструкция") },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    minLines = 5,
                    modifier = Modifier

                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = MaterialTheme.colorScheme.primary)
                )

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(instructionInputText) },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(10.dp),
                elevation = null,
            ) {
                Text(
                    "Сохранить",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(10.dp),
                elevation = null,
            ) {
                Text(
                    "Отмена",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}