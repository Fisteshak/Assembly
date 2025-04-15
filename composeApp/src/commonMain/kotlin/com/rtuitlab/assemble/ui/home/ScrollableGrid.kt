package com.rtuitlab.assemble.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.arrow_left_icon
import assembly.composeapp.generated.resources.arrow_right_icon
import org.jetbrains.compose.resources.painterResource
import kotlin.math.min

@Composable
fun <T> ScrollableGrid(
    items: List<T>,
    rows: Int,
    cols: Int,
    content: @Composable (T) -> Unit,
    placeholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(modifier.height(IntrinsicSize.Min)) {
        val page = remember { mutableIntStateOf(0) }
        val batchSize = rows * cols
        val leftBorder = batchSize * page.value
        val rightBorder = min(items.size, batchSize * (page.value + 1))

        println("Grid: leftBorder $leftBorder, rightBorder $rightBorder")

        val batch = items.slice(leftBorder..<rightBorder)
//
//        Grid(batch, rows, cols, modifier = Modifier) { item ->
//            content(item)
//        }
        NotLazyGrid(
            batch, rows, cols,
            content = { item ->
                content(item)
            },
            placeholder = {
                placeholder()
            }, modifier = Modifier
        )

        Column(
            modifier = Modifier.padding(horizontal = 18.dp).fillMaxHeight().width(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val canScrollRight = page.value < items.size / batchSize
            val canScrollLeft = page.value > 0

            val colorEnabled = Color(0xFFB8C4FF)
            val colorDisabled = Color(0xFFE9E7EF)

            IconButton(
                onClick = { if (canScrollRight) page.value++ },
                enabled = canScrollRight,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_right_icon),
                    contentDescription = "Arrow Right",
                    tint = if (canScrollRight) colorEnabled else colorDisabled
                )
            }
            IconButton(
                onClick = { if (canScrollLeft) page.value-- },
                enabled = canScrollLeft,
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_left_icon),
                    contentDescription = "Arrow Left",
                    tint = if (canScrollLeft) colorEnabled else colorDisabled
                )
            }

        }
    }
}