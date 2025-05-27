package com.rtuitlab.assemble.ui.sound

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rtuitlab.assemble.domain.entities.AssembleComponent


private fun shtukaNumberHandler(n: Long): String {
    val end = n % 10
    val end2 = n % 100

    return "$n штук" +
            if (end2 in 11..19)
                ""
            else
                when (end) {
                    0L, 5L, 6L, 7L, 8L, 9L -> ""
                    1L -> "a"
                    2L, 3L, 4L -> "и"
                    else -> ""
                }
}


@Composable
fun AssembleComponentSoundPanel(
    assembleComponent: AssembleComponent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                assembleComponent.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.weight(1f))
            // TODO обработать окончания
            Text(
                shtukaNumberHandler(assembleComponent.amount),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(Modifier.requiredHeight(6.dp))
        Text(
            "ID: ${assembleComponent.componentId}",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.Start)
        )
    }
}