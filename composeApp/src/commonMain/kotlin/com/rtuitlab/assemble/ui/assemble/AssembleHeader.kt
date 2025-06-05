package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.edit_assemble_icon
import assembly.composeapp.generated.resources.edit_instruction_icon
import assembly.composeapp.generated.resources.volume_icon
import com.rtuitlab.assemble.ui.common.dialogs.EditSingleLineStringDialog
import org.jetbrains.compose.resources.painterResource

@Composable
fun AssembleHeader(
    title: String,
    instruction: String?,
    onTitleChange: (String) -> Unit,
    onInstructionChange: (String) -> Unit,
    onMicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showNameDialog by remember { mutableStateOf(false) }
    var showInstructionDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                showNameDialog = true
            }, modifier = Modifier.requiredHeight(29.dp)) {

                Icon(
                    painter = painterResource(Res.drawable.edit_assemble_icon),
                    contentDescription = "Edit Assembly Name",
                    tint = Color.Unspecified,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showInstructionDialog = true },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.requiredWidth(IntrinsicSize.Max).height(48.dp),
                elevation = null,
                enabled = instruction != null

            ) {
                Icon(
                    painter = painterResource(Res.drawable.edit_instruction_icon),
                    contentDescription = null,
                    modifier = Modifier,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(9.dp))
                Text(
                    text = "Написать инструкцию",
                    softWrap = false,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(10.dp))


            IconButton(
                onClick = { onMicClick() },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .requiredWidth(48.dp)

            ) {
                Icon(
                    painter = painterResource(Res.drawable.volume_icon),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }

        }

        Spacer(modifier = Modifier.height(8.dp))


        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )


        if (showNameDialog) {
            EditSingleLineStringDialog(
                currentString = title,
                label = "Название",
                onDismissRequest = { showNameDialog = false },
                onConfirm = { onTitleChange(it); showNameDialog = false }
            )
        }
        if (showInstructionDialog) {
            EditInstructionDialog(
                currentInstruction = instruction ?: "",
                onDismissRequest = { showInstructionDialog = false },
                onConfirm = { onInstructionChange(it); showInstructionDialog = false }
            )
        }
    }
}