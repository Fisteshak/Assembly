package com.rtuitlab.assemble.ui.sound

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.AssembleStore
import eu.iamkonstantin.kotlin.gadulka.rememberGadulkaLiveState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.getKoin

private enum class State {
    GeneratingSound, NoComponents, FailedToGenerate, Generated
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SoundWindow(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    var state by remember { mutableStateOf(State.GeneratingSound) }
    val store: AssembleStore = getKoin().get()
    val uiState by store.stateFlow.collectAsStateWithLifecycle()
    val assemble = uiState.currentAssemble ?: return
    var currentAssembleComponentIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        store.accept(AssembleStore.Intent.GenerateSoundById(assemble.assembleId))
        store.labels.collect {
            if (it is AssembleStore.Label.GeneratedSound) {
                if (it.assemble != null) {
                    state = if (it.assemble.components == null) {
                        State.FailedToGenerate
                    } else if (it.assemble.components.isEmpty()) {
                        State.NoComponents
                    } else {
                        State.Generated
                    }
                    store.accept(AssembleStore.Intent.SetCurrentAssemble(it.assemble))
                } else {
                    state = State.FailedToGenerate
                }
            }
        }
    }

    Box(
        modifier
            .width(600.dp).height(200.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center

    ) {
        when (state) {
            State.GeneratingSound -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Генерирую звук...",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            State.NoComponents -> {
                Text(
                    "В сборке нет компонентов",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            State.FailedToGenerate -> {
                Text(
                    "Не удалось сгенерировать звук",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            State.Generated -> {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    val gadulka = rememberGadulkaLiveState()
                    var isPlayingStarterSound by remember { mutableStateOf(true) }
                    // playing starting sound for name of assemble and first component
                    LaunchedEffect(Unit) {
                        gadulka.player.play(url = assemble.components!![0].linkToSound!!)
                    }



                    AssembleComponentSoundPanel(
                        assemble.components!![currentAssembleComponentIndex],
                        modifier = Modifier.padding(horizontal = 48.dp, vertical = 30.dp)
                    )

                    val canShowNext = currentAssembleComponentIndex < assemble.components.size - 1
                    val canShowPrevious = currentAssembleComponentIndex > 0
                    ControlsPanel(
                        onNextClick = {
                            currentAssembleComponentIndex++
                            isPlayingStarterSound = false
                            gadulka.player.play(url = assemble.components[currentAssembleComponentIndex].linkToSound!!)
                        },
                        onBackClick = {
                            currentAssembleComponentIndex--
                            isPlayingStarterSound = false
                            gadulka.player.play(url = assemble.components[currentAssembleComponentIndex].linkToSound!!)
                        },
                        nextButtonEnabled = canShowNext,
                        backButtonEnabled = canShowPrevious,
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}



