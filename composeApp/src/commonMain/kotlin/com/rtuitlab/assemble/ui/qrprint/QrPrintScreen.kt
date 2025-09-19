package com.rtuitlab.assemble.ui.qrprint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.ui.container.store.ContainerStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.getKoin

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun QrPrintScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val store: ContainerStore = getKoin().get()
    val uiState by store.stateFlow.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize().padding(top = 30.dp, start = 10.dp, end = 10.dp),
        contentAlignment = Alignment.Center
    ) {


        Scaffold(
            modifier = Modifier.width(700.dp),
            scaffoldState = rememberScaffoldState(),
            topBar = {
                Column {
                    QrPrintHeader(Modifier.fillMaxWidth())
                    Spacer(Modifier.height(30.dp))
                }
            },
            bottomBar = {
                val amount =
                    uiState.containersForPrinting.sumOf { if (it.isChecked) it.amount else 0 }
                Column {
                    Spacer(Modifier.height(24.dp))
                    QrPrintFooter(amount, {
                        val intent =
                            ContainerStore.Intent.Print(uiState.containersForPrinting.filter { it.isChecked })
                        store.accept(intent)
                    }, Modifier.fillMaxWidth())
                }
            }
        ) { innerPadding ->


            QrPrintElementList(
                uiState.containersForPrinting,
                scrollState = rememberScrollState(),
                onCheckedClick = { container, checked ->
                    val intent = ContainerStore.Intent.ChangeContainerForPrinting(
                        container.copy(isChecked = checked)
                    )
                    store.accept(intent)
                },
                onDeleteClick = {
                    val intent = ContainerStore.Intent.DeleteContainerForPrintingByNumber(
                        it
                    )
                    store.accept(intent)
                },
                onAmountChange = { container, amount ->
                    val intent = ContainerStore.Intent.ChangeContainerForPrinting(
                        container.copy(amount = amount)
                    )
                    store.accept(intent)
                },
                modifier = Modifier.padding(innerPadding),
            )


        }


    }
}
