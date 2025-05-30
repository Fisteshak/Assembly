package com.rtuitlab.assemble.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.trash_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun ContainerCard(
    id: String,
    name: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFEEF1F4),
    contentColor: Color = Color.DarkGray
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Box(
        modifier = modifier
            .width(190.dp).height(120.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .hoverable(interactionSource)
    ) {

        Column(
            modifier = Modifier.padding(10.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "№${id}",

                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor
                )
                if (isHovered)
                    Icon(
                        painter = painterResource(resource = Res.drawable.trash_icon),
                        contentDescription = "Delete Button",
                        tint = Color.Unspecified,
                        modifier = Modifier.height(24.dp).clickable { showDeleteDialog = true }
                    )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = contentColor,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
    when {
        showDeleteDialog -> {
            DeleteDialog(
                name,
                onConfirmation = { showDeleteDialog = false; onDelete() },
                onDismissRequest = { showDeleteDialog = false }
            )
        }
    }
}


@Composable
fun PlaceholderContainerCard(modifier: Modifier = Modifier) {
    Box(modifier = modifier.height(120.dp).width(190.dp))
}