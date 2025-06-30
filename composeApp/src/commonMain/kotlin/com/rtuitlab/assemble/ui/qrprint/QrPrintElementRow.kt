package com.rtuitlab.assemble.ui.qrprint

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.box_checked_in_icon
import assembly.composeapp.generated.resources.box_checked_out_icon
import assembly.composeapp.generated.resources.trash_icon
import org.jetbrains.compose.resources.painterResource


@Composable
fun QrPrintElementRow(
    number: String,
    amount: Int,
    isChecked: Boolean,
    onCheckedClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 0.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(4.dp)
                )
                .fillMaxHeight()
                .padding(horizontal = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Контейнер №$number",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )

            Spacer(Modifier.width(110.dp))

            Text(
                "$amount шт",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        }

        Spacer(Modifier.width(66.dp))

        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(4.dp)
                )
                .fillMaxHeight()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onCheckedClick,
                modifier = Modifier
            ) {
                Icon(
                    painter =
                        if (isChecked) {
                            painterResource(Res.drawable.box_checked_in_icon)
                        } else {
                            painterResource(Res.drawable.box_checked_out_icon)
                        },
                    null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            }
        }

        Spacer(Modifier.width(60.dp))
        val colorRed = Color(0xFF9B5657)
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
        ) {
            Icon(
                painter = painterResource(
                    Res.drawable.trash_icon,
                ),
                null,
                modifier = Modifier.size(28.dp),
                tint = colorRed
            )
        }


    }
}
