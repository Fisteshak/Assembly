package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.AssembleStore
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.domain.entities.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.getKoin


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AssembleScreen(
    assemble: Assemble,
    onAmountChange: (Int, Long) -> Unit,
    onNameChange: (Int, String) -> Unit,
    onMenuComponentClick: (Int, Component) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAdd: () -> Unit,
    onAssembleNameChange: (String) -> Unit,
    onInstructionChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val store: AssembleStore = getKoin().get()
    val uiState by store.stateFlow.collectAsStateWithLifecycle()
    val components = uiState.components

    Box(
        modifier = modifier.fillMaxSize().padding(top = 30.dp, start = 10.dp, end = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        val scrollState = rememberScrollState(0)
        var expandedIndex: Int? by remember { mutableStateOf(null) }
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            modifier = Modifier.width(900.dp),
            scaffoldState = rememberScaffoldState(),
            topBar = {
                AssembleHeader(
                    assemble.name,
                    assemble.instruction,
                    { onAssembleNameChange(it) },
                    { onInstructionChange(it) },
                    { }, modifier = Modifier.fillMaxWidth()
                )

            },
            bottomBar = {
                Column {
                    AddButton({
                        onAdd()
                        coroutineScope.launch {
                            delay(100)
                            expandedIndex = assemble.components?.size?.minus(1)
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    }, Modifier.padding(vertical = 16.dp))
                    AssembleFooter(
                        onSaveDraft = {},
                        onPublishAssemble = {
                            if (assemble.components?.all { it.componentId != -1L } == true) {

                                store.accept(AssembleStore.Intent.PublishAssemble(assemble))
                                onNavigateBack()
                            }
                        }, Modifier.fillMaxWidth()
                    )
                }

            }
        ) { innerPadding ->

            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Text(
                    "Добавьте детали",
                    modifier = Modifier.padding(vertical = 14.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                val assembleComponents = assemble.components

                if (assembleComponents != null) {


                    AssembleComponentsList(
                        assembleComponents,
                        components,
                        scrollState = scrollState,
                        expandedIndex = expandedIndex,
                        onAmountChange = { index, amount ->
                            onAmountChange(index, amount)
                        },
                        onNameChange = { index, name ->
                            expandedIndex = index
                            onNameChange(index, name)
                        },
                        onNameClick = { expandedIndex = it },
                        onMenuComponentClick = { index, component ->
                            expandedIndex = null;
                            onMenuComponentClick(index, component)
                        },
                        onMenuDismissRequest = { expandedIndex = null },
                        onDeleteClick = { onDeleteClick(it) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else
                    Text("No Data", fontSize = 20.sp)
            }
        }
    }
}


