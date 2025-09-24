package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.domain.entities.AssembleComponent
import com.rtuitlab.assemble.ui.assemble.store.AssembleStore
import com.rtuitlab.assemble.ui.assemble.store.AssembleStore.Label
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.getKoin


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AssembleScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSoundWindow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val store: AssembleStore = getKoin().get()
    val uiState by store.stateFlow.collectAsStateWithLifecycle()
    val components = uiState.components
    val assemble = uiState.currentAssemble
    val scope = rememberCoroutineScope()
    var isSaving = uiState.isSaving


    var showSaveDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState(0)
    var expandedIndex: Int? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()
    var showPublishAssembleProgressIndicator by remember { mutableStateOf(false) }



    Box(
        modifier = modifier.fillMaxSize().padding(top = 30.dp, start = 10.dp, end = 10.dp),
        contentAlignment = Alignment.Center
    ) {

        if (assemble == null) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )

        } else {

            when {
                //
                showSaveDialog ->
                    SaveAssembleDialog(
                        assembleName = assemble.name,
                        onDismissRequest = { showSaveDialog = false },
                        onDismissButtonClick = { showSaveDialog = false },
                        onConfirmButtonClick = {
                            showSaveDialog = false
                            showPublishAssembleProgressIndicator = true
                        }
                    )


                // this progress indicator is shown before going to soundWindow
                // it publishes assemble, and updates id of current assemble
                // (in case when current assemble was new and had id of -1)
                showPublishAssembleProgressIndicator -> {
                    Dialog(onDismissRequest = {}) {

                        LaunchedEffect(Unit) {
                            store.accept(AssembleStore.Intent.PublishAssemble(assemble))
                            store.labels.collect {
                                if (it is Label.PublishedAssemble) {
                                    showPublishAssembleProgressIndicator = false
                                    store.accept(
                                        AssembleStore.Intent.SetCurrentAssemble(
                                            assemble.copy(assembleId = it.id)
                                        )
                                    )
                                    onNavigateToSoundWindow()
                                }
                            }

                        }
                        Box(
                            modifier = Modifier.width(200.dp).height(140.dp).background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(10.dp)
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    "Сохраняю сборку...",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                        }
                    }
                }
            }

            Scaffold(
                modifier = Modifier.width(900.dp),
                scaffoldState = rememberScaffoldState(),
                topBar = {
                    AssembleHeader(
                        assemble.name,
                        assemble.instruction,
                        onTitleChange = {
                            store.accept(
                                AssembleStore.Intent.SetCurrentAssemble(
                                    assemble.copy(name = it)
                                )
                            )
                        },
                        onInstructionChange = {
                            store.accept(
                                AssembleStore.Intent.SetCurrentAssemble(
                                    assemble.copy(instruction = it)
                                )
                            )
                        },
                        onMicClick = {
                            showSaveDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )


                },
                bottomBar = {
                    Column {
                        AddButton({
                            assemble.components!!.add(AssembleComponent.new())
                            store.accept(
                                AssembleStore.Intent.SetCurrentAssemble(assemble)
                            )
                            coroutineScope.launch {
                                delay(100)
                                expandedIndex = assemble.components.size.minus(1)
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        }, Modifier.padding(vertical = 16.dp))
                        AssembleFooter(
                            onSaveDraft = {},
                            onPublishAssemble = {
                                if (assemble.components?.all { it.componentId != -1L } == true) {

                                    store.accept(AssembleStore.Intent.PublishAssemble(assemble))
                                    isSaving = true

                                }
                            },
                            isNew = assemble.assembleId == -1L,
                            isSaving = isSaving,
                            Modifier.fillMaxWidth()
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
                                assemble.components[index] =
                                    assemble.components[index].copy(
                                        amount = amount,
                                        linkToSound = null
                                    )
                                store.accept(
                                    AssembleStore.Intent.SetCurrentAssemble(
                                        assemble
                                    )
                                )
                            },
                            onNameChange = { index, name ->
                                expandedIndex = index
                                assemble.components[index] =
                                    assemble.components[index].copy(
                                        name = name,
                                        componentId = -1
                                    )
                                store.accept(
                                    AssembleStore.Intent.SetCurrentAssemble(
                                        assemble
                                    )
                                )
                            },
                            onNameClick = { expandedIndex = it },
                            onMenuComponentClick = { index, component ->
                                expandedIndex = null;
                                assemble.components[index] = AssembleComponent(
                                    component.id,
                                    component.name,
                                    assemble.components[index].amount,
                                    null,
                                    null,
                                    false
                                )
                                store.accept(
                                    AssembleStore.Intent.SetCurrentAssemble(
                                        assemble
                                    )
                                )
                            },
                            onMenuDismissRequest = { expandedIndex = null },
                            onDeleteClick = {
                                assemble.components.removeAt(it)
                                store.accept(
                                    AssembleStore.Intent.SetCurrentAssemble(
                                        assemble
                                    )
                                )
                            },

                            modifier = Modifier.fillMaxWidth(),
                        )
                    } else
                        Text("No Data", fontSize = 20.sp)
                }
            }


        }

        SnackbarHost(
            hostState = uiState.snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


