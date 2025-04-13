package com.rtuitlab.assembly.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.assemble_icon
import assembly.composeapp.generated.resources.contatiner_icon
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assembly.di.AppModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val store = AppModule.getMainStore()

    val uiState by store.stateFlow.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {

        Row(modifier = Modifier.padding(20.dp).fillMaxHeight()) {
            SideButtonPanel(
                onAssembleClick = {},
                onContainerClick = {},
                onQrCodeClick = {},
                modifier = Modifier
            )
            Column() {
                AssemblyCard(
                    title = "10",
                    mainText = "sasdd"
                )
            }
        }
    }
}


@Composable
fun BigSideButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp))
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun SideButtonPanel(
    onAssembleClick: () -> Unit,
    onContainerClick: () -> Unit,
    onQrCodeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
    ) {
        BigSideButton(
            modifier = Modifier.width(230.dp).padding(16.dp),
            onClick = onAssembleClick,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Добавить сборку",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Icon(
                        painter = painterResource(Res.drawable.assemble_icon),
                        contentDescription = "Добавить сборку",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            },
        )

        BigSideButton(
            modifier = Modifier.width(230.dp).height(200.dp).padding(16.dp),
            onClick = onContainerClick,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Добавить\nконтейнер",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleMedium

                    )
                    Icon(
                        painter = painterResource(Res.drawable.contatiner_icon),
                        contentDescription = "Добавить контейнер",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            },
        )

        Spacer(Modifier.weight(1f))

        BigSideButton(
            modifier = Modifier.width(230.dp).padding(16.dp),
            onClick = onQrCodeClick,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,

                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Печать QR-кодов",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleMedium

                    )
                }
            },
        )
    }
}