package com.rtuitlab.assemble.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import com.rtuitlab.assemble.domain.entities.Assemble


@Composable
fun <T> Grid(
    items: List<T>,
    rows: Int,
    cols: Int,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    val batchSize = rows * cols

    LazyHorizontalGrid(
        rows = GridCells.Fixed(rows),
        contentPadding = PaddingValues(0.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false,
        modifier = modifier
    ) {
        items(batchSize) { index ->
            if (index < items.size)
                content(items[index])
            else
                PlaceholderAssembleCard()

        }
    }
}

@Composable
fun <T> NotLazyGrid(
    items: List<T>,
    rows: Int,
    cols: Int,
    content: @Composable (T) -> Unit,
    placeholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (col in 0 until cols) {
                    val index = row * cols + col
                    if (index < items.size) {
                        content(items[index])
                    } else {
                        placeholder()
                    }
                }
            }
            if (row < rows - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}