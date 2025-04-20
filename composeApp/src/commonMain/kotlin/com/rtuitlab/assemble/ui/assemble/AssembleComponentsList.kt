package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rtuitlab.assemble.domain.entities.AssembleComponent

@Composable
fun AssembleComponentsList(
    components: List<AssembleComponent>,
    modifier: Modifier = Modifier,
) {
    val stateVertical = rememberScrollState(0)
    Box(modifier = modifier) {

        Box(
            modifier = Modifier.verticalScroll(stateVertical).padding(end = 24.dp, bottom = 12.dp)
        ) {
            Column(modifier = Modifier) {


                for ((index, component) in components.withIndex()) {
                    AssembleComponentRow(
                        component,
                        {},
                        {},
                        {},
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(end = 20.dp)
                    )

                    if (index != components.size - 1) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 22.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
    }

}