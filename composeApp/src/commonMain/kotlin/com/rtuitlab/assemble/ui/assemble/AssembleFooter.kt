package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp


@Composable
fun AssembleFooter(
    onSaveDraft: () -> Unit,
    onPublishAssemble: () -> Unit,
    isNew: Boolean,
    isSaving: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .requiredHeight(64.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly


    ) {
        Button(
            onClick = { onSaveDraft() },
            modifier = Modifier.width(300.dp).height(45.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(10.dp),
            elevation = null

        ) {
            Text(
                "Сохранить черновик",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(Modifier.width(14.dp))
        Button(
            onClick = { onPublishAssemble() },
            modifier = Modifier.width(300.dp).height(45.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(10.dp),
            elevation = null,
            enabled = !isSaving

            ) {
            Text(
                if (isNew) "Опубликовать сборку" else "Сохранить сборку",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}