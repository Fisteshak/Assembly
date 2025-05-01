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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.minus_icon
import assembly.composeapp.generated.resources.plus_icon
import assembly.composeapp.generated.resources.trash_icon
import com.rtuitlab.assemble.domain.entities.AssembleComponent
import com.rtuitlab.assemble.domain.entities.Component
import org.jetbrains.compose.resources.painterResource


@Composable
fun QuantitySelector(
    count: Long,
    onCountChange: (Long) -> Unit,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,

        modifier = Modifier //.padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        val colorEnabled = Color(0xFFB8C4FF)
        val colorDisabled = Color(0xFFE9E7EF)
        IconButton(
            onClick = { if (count > 0) onCountChange(count - 1) },
            enabled = count > 0
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
                color = textColor,
                fontSize = 16.sp,
            )
        }

        IconButton(onClick = { onCountChange(count + 1) }) {
            Icon(
                painter = painterResource(Res.drawable.plus_icon),
                contentDescription = "Increase quantity",
                tint = colorEnabled
            )
        }
    }
}



@Composable
fun AssembleComponentRow(
    assembleComponent: AssembleComponent,
    menuExpanded: Boolean,
    components: List<Component>,
    onTextChange: (String) -> Unit,
    onAmountChange: (Long) -> Unit,
    onDeleteClick: () -> Unit,
    onDismissRequest: () -> Unit,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 0.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            color = backgroundColor,
            modifier = Modifier.weight(1f).fillMaxHeight(),

            ) {
            BasicTextField(
                value = assembleComponent.name,
                onValueChange = { onTextChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = 16.sp
                ),
                singleLine = true
            )
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { onDismissRequest() },
                modifier = Modifier
            ) {
                components.forEach { component ->

                    DropdownMenuItem(
                        onClick = { /* Do something... */ },
                        modifier = Modifier
                    ) {
                        Text(component.name)
                    }
                }

            }


        }

        QuantitySelector(
            count = assembleComponent.amount,
            onCountChange = { onAmountChange(it) },
            backgroundColor = backgroundColor,
            textColor = textColor
        )


        IconButton(onClick = onDeleteClick) {
            Icon(
                painter = painterResource(Res.drawable.trash_icon),
                contentDescription = "Delete item",
                tint = Color.Unspecified
            )
        }
    }
}
