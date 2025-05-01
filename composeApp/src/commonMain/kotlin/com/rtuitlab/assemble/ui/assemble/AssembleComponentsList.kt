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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rtuitlab.assemble.domain.entities.AssembleComponent
import com.rtuitlab.assemble.domain.entities.Component

@Composable
fun AssembleComponentsList(
    assembleComponents: List<AssembleComponent>,
    components: List<Component>,
    modifier: Modifier = Modifier,
) {
    val stateVertical = rememberScrollState(0)
    Box(modifier = modifier) {

        Box(
            modifier = Modifier.verticalScroll(stateVertical).padding(end = 24.dp, bottom = 12.dp)
        ) {
            Column(modifier = Modifier) {

                var expandedIndex: Int? by remember { mutableStateOf(null) }
                for ((index, component) in assembleComponents.withIndex()) {
                    Box(Modifier) {

                        AssembleComponentRow(
                            assembleComponent = component,
                            menuExpanded = expandedIndex == index,
                            components = components,
                            onTextChange = { expandedIndex = index; },
                            onAmountChange = {},
                            onDeleteClick = {},
                            onDismissRequest = { expandedIndex = null },
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(end = 20.dp)
                        )


                    }

                    if (index != assembleComponents.size - 1) {
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