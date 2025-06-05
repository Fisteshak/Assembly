package com.rtuitlab.assemble.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.AssembleStore
import com.rtuitlab.assemble.ui.container.store.ContainerStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.getKoin


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAssembleClick: (Long) -> Unit,
    onContainerClick: (String) -> Unit,
) {
    val assembleStore: AssembleStore = getKoin().get()
    val assembleStoreUiState by assembleStore.stateFlow.collectAsStateWithLifecycle()
    val assemblies = assembleStoreUiState.assemblies

    val containerStore: ContainerStore = getKoin().get()
    val containerStoreUiState by containerStore.stateFlow.collectAsStateWithLifecycle()
    val containers = containerStoreUiState.containers



    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(modifier = Modifier.width(IntrinsicSize.Min)) {

            HomeHeader(
                "Сборки",
                modifier = Modifier.fillMaxWidth().padding(start = 6.dp, end = 14.dp)
            )
            Box(Modifier.width(IntrinsicSize.Min), contentAlignment = Alignment.Center) {

                ScrollableGrid(
                    assemblies,
                    rows = 2, cols = 4,
                    modifier = Modifier.padding(10.dp),
                    content = { item ->
                        AssembleCard(
                            item,
                            onClick = {
                                assembleStore.accept(AssembleStore.Intent.SetCurrentAssemble(null))
                                assembleStore.accept(AssembleStore.Intent.FetchAssembleById(item.assembleId))
                                onAssembleClick(item.assembleId)
                            },
                            onDelete = {
                                assembleStore.accept(AssembleStore.Intent.DeleteAssembleById(item.assembleId))
                            },
                            modifier = Modifier
                        )
                    },
                    placeholder = {
                        PlaceholderCard()
                    }
                )
            }

            HomeHeader(
                "Контейнеры",
                modifier = Modifier.fillMaxWidth().padding(start = 6.dp, end = 14.dp)
            )


            Box(Modifier.width(IntrinsicSize.Min), contentAlignment = Alignment.Center) {
                if (containers == null)
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                ScrollableGrid(
                    containers ?: emptyList(),
                    rows = 2, cols = 4,
                    modifier = Modifier.padding(10.dp),
                    content = { item ->
                        ContainerCard(
                            id = item.number,
                            name = item.formatAmount(),
                            onClick = {
                                containerStore.accept(
                                    ContainerStore.Intent.SetCurrentContainer(null)
                                )
                                containerStore.accept(
                                    ContainerStore.Intent.GetAndSetCurrentContainerByNumber(
                                        item.number
                                    )
                                )
                                onContainerClick(item.number)
                            },
                            onDelete = {
//                            assembleStore.accept(AssembleStore.Intent.DeleteAssembleById(item.assembleId))
                            },
                            modifier = Modifier
                        )
                    },
                    placeholder = {
                        PlaceholderCard()
                    }
                )
            }

        }
    }
}


@Composable
fun HomeHeader(title: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

