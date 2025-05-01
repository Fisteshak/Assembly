package com.rtuitlab.assemble.ui.assemble

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.MainStore
import com.rtuitlab.assemble.domain.entities.Assemble
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.getKoin

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AssembleScreen(
    assembleId: Long?,
    modifier: Modifier = Modifier
) {
    val store: MainStore = getKoin().get()


    val uiState by store.stateFlow.collectAsStateWithLifecycle()
    val assemblies = uiState.assemblies
    val components = uiState.components

    val assemble = assemblies.find { assembleId == it.assembleId } ?: Assemble(
        -1,
        "New Assemble",
        "",
        0,
        null,
        null,
        0,
        emptyList()
    )


    Box(
        modifier = modifier.fillMaxSize().padding(top = 30.dp, start = 10.dp, end = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            modifier = Modifier.width(900.dp),
            scaffoldState = rememberScaffoldState(),
            topBar = {
                AssembleHeader(assemble.name, { }, { }, { }, modifier = Modifier.fillMaxWidth())

            },
            bottomBar = {
                Column {
                    AddButton({}, Modifier.padding(vertical = 16.dp))
                    AssembleFooter({}, {}, Modifier.fillMaxWidth())
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
                if (assemble.components != null)
                    AssembleComponentsList(
                        assemble.components,
                        components,
                        modifier = Modifier.fillMaxWidth()
                    )
                else
                    Text("No Data", fontSize = 20.sp)
            }
        }
    }
}


