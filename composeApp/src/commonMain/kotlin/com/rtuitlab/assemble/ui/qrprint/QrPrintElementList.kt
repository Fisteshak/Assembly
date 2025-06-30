package com.rtuitlab.assemble.ui.qrprint

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QrPrintElementList(
    elements: List<Unit>,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {

    Box(modifier = modifier) {

        Box(
            modifier = Modifier.verticalScroll(scrollState).padding(end = 24.dp, bottom = 12.dp)
        ) {
            Column(modifier = Modifier) {
                for ((index, component) in elements.withIndex()) {
                    Box(Modifier) {
                        QrPrintElementRow(
                            number = "1",
                            amount = 2,
                            isChecked = false,
                            onCheckedClick = {},
                            onDeleteClick = {},
                            modifier = Modifier,
                        )
                    }
                    if (index != elements.size - 1) {
                        Spacer(Modifier.height(36.dp))
                    }
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }

}