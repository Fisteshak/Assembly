package com.rtuitlab.assemble.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.rtuitlab.assemble.data.createSampleAssembles
import com.rtuitlab.assemble.di.AppModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HomeScreenPreview() {
    Text("Hello!!")
}

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAssembleClick: (Long) -> Unit
) {
    val store = AppModule.getMainStore()

    val uiState by store.stateFlow.collectAsStateWithLifecycle()


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(modifier = Modifier.width(IntrinsicSize.Min)) {

            val data = createSampleAssembles(20)
            HomeHeader(
                "Сборки",
                modifier = Modifier.fillMaxWidth().padding(start = 6.dp, end = 14.dp)
            )
            ScrollableGrid(
                data,
                rows = 2, cols = 4,
                modifier = Modifier.padding(10.dp),
                content = { item ->
                    AssembleCard(item, { onAssembleClick(item.assembleId) }, modifier = Modifier)
                },
                placeholder = {
                    PlaceholderAssembleCard()
                }
            )
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

