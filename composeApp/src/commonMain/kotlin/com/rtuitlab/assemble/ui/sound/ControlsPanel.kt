package com.rtuitlab.assemble.ui.sound

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ControlsPanel(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    nextButtonEnabled: Boolean,
    backButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(
            onClick = { onBackClick() },
            enabled = backButtonEnabled,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(10.dp),
        )
        {
            Text(
                "Назад", color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                softWrap = false
            )
        }
        Spacer(Modifier.requiredWidth(20.dp))
        Button(
            onClick = { onNextClick() },
            enabled = nextButtonEnabled,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(10.dp)
        )
        {
            Text(
                "Далее", color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                softWrap = false
            )
        }
    }

}