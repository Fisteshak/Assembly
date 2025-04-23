package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.plus2_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.surfaceVariant),
        contentPadding = PaddingValues(10.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
        elevation = null

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.plus2_icon),
                contentDescription = "Add Component",
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(10.dp))
            Text(
                "Добавить",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}