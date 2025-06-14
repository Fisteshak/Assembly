package com.rtuitlab.assemble.ui.container

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.ui.common.QuantitySelector

@Composable
fun ContainerRow(
    component: Component,
    amount: Long,
    menuExpanded: Boolean,
    components: List<Component>,
    onTextChange: (String) -> Unit,
    onTextClick: () -> Unit,
    onAmountChange: (Long) -> Unit,
    onMenuDismissRequest: () -> Unit,
    onMenuComponentClick: (Component) -> Unit,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = 0.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            color = backgroundColor,
            modifier = Modifier
                .weight(1f),
        ) {

            BasicTextField(
                value = component.name,
                onValueChange = { onTextChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                textStyle = TextStyle(
                    color = if (component.id != -1L) textColor else MaterialTheme.colorScheme.error,
                    fontSize = 16.sp
                ),
                singleLine = true,
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    onTextClick()
                                }
                            }
                        }
                    }
            )
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { onMenuDismissRequest() },
                properties = PopupProperties(focusable = false),
                modifier = Modifier
            ) {
                components.forEach { component ->

                    DropdownMenuItem(
                        onClick = { onMenuComponentClick(component) },
                        modifier = Modifier
                    ) {
                        Text(component.name)
                    }
                }

            }


        }

        QuantitySelector(
            count = amount,
            onCountChange = { onAmountChange(it) },
            backgroundColor = backgroundColor,
            textColor = textColor,
            modifier = Modifier
                .width(200.dp)

        )

    }
}
