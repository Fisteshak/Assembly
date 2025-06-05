package com.rtuitlab.assemble.ui.common.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditSingleLineStringDialog(
    currentString: String,
    label: String,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    confirmText: String = "Сохранить",
    dismissText: String = "Отмена",
) {
    var nameInputText by remember { mutableStateOf(currentString) }

    AlertDialog(
        onDismissRequest = onDismissRequest,

        title = null, text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nameInputText,
                    onValueChange = { nameInputText = it },
                    label = { Text(label) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = MaterialTheme.colorScheme.primary)
                )
            }
        }, confirmButton = {
            Button(
                onClick = { onConfirm(nameInputText); },
                modifier = Modifier,
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
        }, dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                modifier = Modifier,
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