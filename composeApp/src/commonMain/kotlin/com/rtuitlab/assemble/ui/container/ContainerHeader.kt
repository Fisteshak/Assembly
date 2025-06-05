package com.rtuitlab.assemble.ui.container

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
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
import com.rtuitlab.assemble.ui.common.dialogs.EditSingleLineStringDialog
import org.jetbrains.compose.resources.painterResource

@Composable
fun ContainerHeader(
    number: String,
    onTitleChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showNameDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Контейнер №$number",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                showNameDialog = true
            }, modifier = Modifier.requiredHeight(29.dp)) {

                Icon(
                    painter = painterResource(Res.drawable.edit_assemble_icon),
                    contentDescription = "Edit Container Name",
                    tint = Color.Unspecified,
                    modifier = Modifier
                )
            }
        }


        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )


        if (showNameDialog) {
            EditSingleLineStringDialog(
                currentString = number,
                label = "Контейнер №",
                onDismissRequest = { showNameDialog = false },
                onConfirm = { onTitleChange(it); showNameDialog = false }
            )
        }
    }
}