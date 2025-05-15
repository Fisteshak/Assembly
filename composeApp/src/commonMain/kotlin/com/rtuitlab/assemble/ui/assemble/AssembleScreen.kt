package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.AssembleStore
import com.rtuitlab.assemble.domain.entities.AssembleComponent
import eu.iamkonstantin.kotlin.gadulka.rememberGadulkaState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.getKoin


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AssembleScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val store: AssembleStore = getKoin().get()
    val uiState by store.stateFlow.collectAsStateWithLifecycle()
    val components = uiState.components
    val assemble = uiState.currentAssemble
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }

    val player = rememberGadulkaState()

    Box(
        modifier = modifier.fillMaxSize().padding(top = 30.dp, start = 10.dp, end = 10.dp),
        contentAlignment = Alignment.Center
    ) {

        if (assemble == null) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )

        } else {
            val scrollState = rememberScrollState(0)
            var soundPageExpanded by remember { mutableStateOf(false) }
            var expandedIndex: Int? by remember { mutableStateOf(null) }
            val coroutineScope = rememberCoroutineScope()
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

                            soundPageExpanded = true
                            store.accept(AssembleStore.Intent.PublishAssemble(assemble))
                            scope.launch {
                                store.labels.collect {
                                    if (it is AssembleStore.Label.PublishedAssemble) {
                                        store.accept(AssembleStore.Intent.GenerateSoundById(it.id))
                                    }
                                    if (it is AssembleStore.Label.GeneratedSound) {
                                        store.accept(AssembleStore.Intent.SetCurrentAssemble(it.assemble))
                                        soundPageExpanded = false

                                    }

                                }
                            }

                        },
                        onControlClick = { println("playing"); player.play(url = assemble.linkToSound!!) },
                        showControls = assemble.linkToSound != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    when {
                        soundPageExpanded -> {
                            Dialog(
                                onDismissRequest = { soundPageExpanded = false }
                            ) {


                                Box(
                                    Modifier
                                        .width(200.dp).height(100.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.surface,
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center

                                ) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }


                },
                bottomBar = {
                    Column {
                        AddButton({
                            assemble.components!!.add(
                                AssembleComponent(
                                    -1,
                                    "",
                                    1,
                                    null,
                                    null,
                                    true
                                )
                            )
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
                                    scope.launch {
                                        store.labels.collect {
                                            if (it is AssembleStore.Label.PublishedAssemble) {
                                                onNavigateBack()
                                            }
                                        }

                                    }
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
                            onPlayButtonClick = { index ->

                                player.play(url = assemble.components[index].linkToSound!!)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    } else
                        Text("No Data", fontSize = 20.sp)
                }
            }


        }


    }
}


