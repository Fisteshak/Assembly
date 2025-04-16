package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.minus_icon
import assembly.composeapp.generated.resources.plus_icon
import assembly.composeapp.generated.resources.trash_icon
import org.jetbrains.compose.resources.painterResource

// Color matching the image (adjust as needed)
val lightPurple = Color(0xFFEAE4F2)
val contentPurple = Color(0xFF625B71) // A slightly darker purple for content

/**
 * Composable for the quantity selector (+ / - buttons and count).
 */
@Composable
fun QuantitySelector(
    count: Int,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = lightPurple,
    contentColor: Color = contentPurple
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        // Reduced padding inside the counter surface to make it more compact
        modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        val colorEnabled = Color(0xFFB8C4FF)
        val colorDisabled = Color(0xFFE9E7EF)
        IconButton(
            onClick = { if (count > 0) onCountChange(count - 1) }, // Allow going to 0 or set min to 1
            enabled = count > 0 // Disable if count is 0 (or 1 if that's the minimum)
        ) {
            Icon(
                painter = painterResource(Res.drawable.minus_icon),
                contentDescription = "Decrease quantity",
                tint = if (count > 0) colorEnabled else colorDisabled
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(color = backgroundColor)
                .padding(horizontal = 36.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = "$count",
                modifier = Modifier.defaultMinSize(minWidth = 24.dp),

                textAlign = TextAlign.Center,
                color = contentColor,
                fontSize = 16.sp,
            )
        }
        // Plus Button
        IconButton(onClick = { onCountChange(count + 1) }) {
            Icon(
                painter = painterResource(Res.drawable.plus_icon),
                contentDescription = "Increase quantity",
                tint = colorEnabled
            )
        }
    }
}


/**
 * The main composable row containing TextField, QuantitySelector, and Delete button.
 */
@Composable
fun ComponentRow(
    modifier: Modifier = Modifier,
    initialText: String = "Винт М-20",
    initialCount: Int = 1,
    onTextChange: (String) -> Unit = {}, // Callback for text changes
    onCountChange: (Int) -> Unit = {}, // Callback for count changes
    onDeleteClick: () -> Unit = {}, // Callback for delete action
    textFieldBackgroundColor: Color = lightPurple,
    counterBackgroundColor: Color = lightPurple,
    contentColor: Color = contentPurple
) {
    // Internal state for the component
    var text by remember { mutableStateOf(initialText) }
    var count by remember { mutableStateOf(initialCount) }

    // Use LaunchedEffect to call callbacks when internal state changes
    LaunchedEffect(text) {
        onTextChange(text)
    }
    LaunchedEffect(count) {
        onCountChange(count)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            color = textFieldBackgroundColor,
            modifier = Modifier.weight(1f),

            ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                textStyle = TextStyle(
                    color = contentColor,
                    fontSize = 16.sp
                ),
                cursorBrush = SolidColor(contentColor), // Set cursor color
                singleLine = true
            )
        }

        // Counter
        QuantitySelector(
            count = count,
            onCountChange = { count = it }, // Update internal state
            backgroundColor = counterBackgroundColor,
            contentColor = contentColor
        )

        // Delete Button
        IconButton(onClick = onDeleteClick) {
            Icon(
                painter = painterResource(Res.drawable.trash_icon),
                contentDescription = "Delete item",
                tint = contentColor
            )
        }
    }
}
