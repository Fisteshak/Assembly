package com.rtuitlab.assemble.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.minus_icon
import assembly.composeapp.generated.resources.plus_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun QuantitySelector(
    count: Long,
    onCountChange: (Long) -> Unit,
    backgroundColor: Color,
    textColor: Color,
    buttonEnabledColor: Color = Color(0xFFB8C4FF),
    buttonDisabledColor: Color = Color(0xFFE9E7EF),
    modifier: Modifier = Modifier.Companion,
) {

    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier = modifier
            .height(IntrinsicSize.Min)

    ) {
        val colorEnabled = buttonEnabledColor
        val colorDisabled = buttonDisabledColor
        IconButton(
            onClick = { if (count > 1) onCountChange(count - 1) },
            enabled = count > 1,
        ) {
            Icon(
                painter = painterResource(Res.drawable.minus_icon),
                contentDescription = "Decrease quantity",
                tint = if (count > 1) colorEnabled else colorDisabled
            )
        }

        Box(
            contentAlignment = Alignment.Companion.Center,
            modifier = Modifier.Companion
                .background(color = backgroundColor)
                .padding(horizontal = 36.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = "$count",
                modifier = Modifier.Companion.defaultMinSize(minWidth = 24.dp),

                textAlign = TextAlign.Companion.Center,
                color = textColor,
                fontSize = 16.sp,
            )
        }

        IconButton(
            onClick = { onCountChange(count + 1) },
            modifier = Modifier.Companion
        ) {
            Icon(
                painter = painterResource(Res.drawable.plus_icon),
                contentDescription = "Increase quantity",
                tint = colorEnabled
            )
        }
    }
}