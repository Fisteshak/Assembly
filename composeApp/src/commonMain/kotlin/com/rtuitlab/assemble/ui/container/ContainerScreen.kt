package com.rtuitlab.assemble.ui.container

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.ui.common.QuantitySelector
import com.rtuitlab.assemble.ui.container.store.ContainerStore
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Intent
import io.github.alexzhirkevich.qrose.ImageFormat
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrColors
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import io.github.alexzhirkevich.qrose.toByteArray
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.getKoin


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ContainerScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val store: ContainerStore = getKoin().get()
    val uiState by store.stateFlow.collectAsStateWithLifecycle()

    val currentContainer = uiState.currentContainer


    var menuExpanded: Boolean by remember { mutableStateOf(false) }




    Box(
        modifier = modifier.fillMaxSize().padding(top = 30.dp, start = 10.dp, end = 10.dp),

        contentAlignment = Alignment.Center
    ) {



        Column(
            modifier = modifier.widthIn(max = 800.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (currentContainer == null) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
//                require(currentContainer.container != null)
                val container = currentContainer.container
                val components = uiState.components
                var isSaving = uiState.isSaving


                ContainerHeader(
                    currentContainer.container.number,
                    onTitleChange = {
                        store.accept(
                            Intent.SetCurrentContainer(
                                currentContainer.copy(container = container.copy(number = it))
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Аудитория хранения",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 14.dp),
                )

                RoomTextField(
                    room = currentContainer.container.room,
                    onTextChange = {
                        store.accept(
                            Intent.SetCurrentContainer(
                                currentContainer.copy(container = container.copy(room = it))
                            )
                        )
                    },
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(end = 220.dp),
                )

                Text(
                    "Добавьте детали",
                    modifier = Modifier.padding(vertical = 14.dp),
                    style = MaterialTheme.typography.titleLarge
                )


                ContainerRow(
                    component = currentContainer.containerComponent,
                    amount = container.amount,
                    menuExpanded = menuExpanded,
                    components = components.filter { it.name.contains(currentContainer.containerComponent.name) },
                    onTextChange = {
                        store.accept(
                            Intent.SetCurrentContainerComponent(
                                currentContainer.containerComponent.copy(
                                    name = it,
                                    id = -1L
                                )
                            )
                        )

                    },
                    onTextClick = { menuExpanded = true },
                    onAmountChange = {
                        store.accept(
                            Intent.SetCurrentContainer(
                                currentContainer.copy(container = container.copy(amount = it))
                            )
                        )
                    },
                    onMenuDismissRequest = { menuExpanded = false },
                    onMenuComponentClick = {

                        store.accept(
                            Intent.SetCurrentContainer(
                                currentContainer.copy(
                                    container = container.copy(
                                        componentId = it.id
                                    ),
                                    containerComponent = it
                                )
                            )
                        )
                        menuExpanded = false
                    },
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(end = 20.dp)
                )

                Spacer(Modifier.height(20.dp))


                Text(
                    "QR-код для печати",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(20.dp))

                val isNew = currentContainer.number == null


                if (isNew) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(220.dp)
                            .border(width = Dp.Hairline, color = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            "Сохраните контейнер для генерации QR-кода",
                            modifier = Modifier,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Row {
                        val qrCode = rememberQrCodePainter(
                            "https://assemble.rtuitlab.dev/container/${currentContainer.number}",
                            colors = QrColors(dark = QrBrush.solid(MaterialTheme.colorScheme.primary))
                        )

                        Image(
                            painter = qrCode,
                            contentDescription = "QR code of container",
                            modifier = Modifier.size(220.dp)
                        )
                        Spacer(Modifier.width(36.dp))

                        Column(
                            modifier = Modifier
                                .width(300.dp)
                                .height(220.dp)
                                .shadow(1.dp, shape = RoundedCornerShape(14.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            var quantity by remember { mutableStateOf(1L) }
                            QuantitySelector(
                                quantity,
                                onCountChange = { quantity = it },
                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                                textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                buttonEnabledColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                buttonDisabledColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )


                            Button(
                                onClick = {
                                    store.accept(
                                        Intent.Print(
                                            qrCode.toByteArray(
                                                qrCode.intrinsicSize.width.toInt(),
                                                qrCode.intrinsicSize.height.toInt(),
                                                ImageFormat.PNG
                                            ).toList()
                                        )
                                    )
                                },
                                modifier = Modifier,
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.secondaryContainer),
                                shape = RoundedCornerShape(8.dp),
                                elevation = null,
                                enabled = !isSaving

                            ) {
                                Text(
                                    "Отправить на печать",
                                    modifier = Modifier
                                        .padding(vertical = 6.dp, horizontal = 18.dp),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }


                        }

                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (!isNew)
                            store.accept(
                                Intent.UpdateCurrentContainerByNumber(
                                    container,
                                    // TODO remove null assert
                                    currentContainer.number
                                )
                            )
                        else
                            store.accept(Intent.CreateCurrentContainer(container))

                    },
                    modifier = Modifier.width(220.dp).height(45.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp),
                    elevation = null,
                    enabled = !isSaving

                ) {
                    Text(
                        if (!isNew) "Сохранить" else "Создать",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }

        SnackbarHost(
            hostState = uiState.snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


