package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rtuitlab.assemble.AssembleStore
import com.rtuitlab.assemble.domain.entities.AssembleComponent
import com.rtuitlab.assemble.domain.entities.Component
import org.koin.compose.getKoin

@Composable
fun AssembleComponentsList(
    assembleComponents: List<AssembleComponent>,
    components: List<Component>,
    expandedIndex: Int?,
    scrollState: ScrollState,
    onAmountChange: (Int, Long) -> Unit,
    onNameChange: (Int, String) -> Unit,
    onNameClick: (Int) -> Unit,
    onMenuComponentClick: (Int, Component) -> Unit,
    onMenuDismissRequest: () -> Unit,
    onDeleteClick: (Int) -> Unit,
    onPlayButtonClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val store: AssembleStore = getKoin().get()

    Box(modifier = modifier) {

        Box(
            modifier = Modifier.verticalScroll(scrollState).padding(end = 24.dp, bottom = 12.dp)
        ) {
            Column(modifier = Modifier) {

                for ((index, component) in assembleComponents.withIndex()) {
                    Box(Modifier) {

                        AssembleComponentRow(
                            assembleComponent = component,
                            menuExpanded = expandedIndex == index,
                            components = components.filter { it.name.contains(component.name) },
                            onTextChange = { onNameChange(index, it) },
                            onTextClick = { onNameClick(index) },
                            onAmountChange = { onAmountChange(index, it) },
                            onDeleteClick = { onDeleteClick(index) },
                            onMenuDismissRequest = { onMenuDismissRequest() },
                            onMenuComponentClick = { onMenuComponentClick(index, it) },
                            onPlayButtonClick = { onPlayButtonClick(index) },
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
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }

}