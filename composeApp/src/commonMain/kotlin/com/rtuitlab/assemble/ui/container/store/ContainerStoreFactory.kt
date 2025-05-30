package com.rtuitlab.assemble.ui.container.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.rtuitlab.assemble.data.api.AppResult
import com.rtuitlab.assemble.domain.entities.Container
import com.rtuitlab.assemble.domain.usecases.GetContainerByNumberUseCase
import com.rtuitlab.assemble.domain.usecases.GetContainersUseCase
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Intent
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Label
import com.rtuitlab.assemble.ui.container.store.ContainerStore.State
import kotlinx.coroutines.launch


internal class ContainerStoreFactory(
    private val storeFactory: StoreFactory,
    private val getContainersUseCase: GetContainersUseCase,
    private val getContainerByNumberUseCase: GetContainerByNumberUseCase
) {

    private sealed interface Action {
        data class GetContainers(val room: String? = null) : Action

    }

    private sealed interface Msg {
        data class SetContainers(val containers: List<Container>) : Msg
        data class SetCurrentContainer(val container: State.CurrentContainer?) : Msg
    }

    fun create(): ContainerStore =
        object : ContainerStore,
            Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
                name = "CounterStore",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Action.GetContainers()),
                executorFactory = coroutineExecutorFactory {
                    onIntent<Intent.GetContainers> {
                        launch {
                            val result: AppResult<List<Container>> = getContainersUseCase(it.room)
                            when (result) {
                                is AppResult.Success -> dispatch(Msg.SetContainers(result.data))
                                is AppResult.Failure -> println("error: ${result.error}")
                            }
                        }
                    }

                    onIntent<Intent.GetAndSetCurrentContainerByNumber> {
                        launch {
                            // set container number we are expecting to get
                            dispatch(
                                Msg.SetCurrentContainer(
                                    State.CurrentContainer(it.number, null)
                                )
                            )

                            val result: AppResult<Container> =
                                getContainerByNumberUseCase(it.number)
                            when (result) {
                                is AppResult.Success -> {
                                    // if there was another request and we are expecting another number, do nothing
                                    if (state().currentContainer?.expectedNumber == result.data.number)
                                        dispatch(
                                            Msg.SetCurrentContainer(
                                                state().currentContainer?.copy(
                                                    container = result.data
                                                )
                                            )
                                        )
                                }

                                is AppResult.Failure -> {
                                    TODO()
                                }
                            }
                        }
                    }

                    onAction<Action.GetContainers> {
                        launch {
                            val result: AppResult<List<Container>> = getContainersUseCase(it.room)
                            when (result) {
                                is AppResult.Success -> dispatch(Msg.SetContainers(result.data))
                                is AppResult.Failure -> println("error: ${result.error}")
                            }
                        }
                    }

                    onIntent<Intent.SetCurrentContainer> {
                        dispatch(Msg.SetCurrentContainer(it.container))
                    }


                },
                reducer = { msg ->
                    when (msg) {
                        is Msg.SetContainers -> {
                            copy(containers = msg.containers)
                        }

                        is Msg.SetCurrentContainer -> {
                            copy(currentContainer = msg.container)
                        }
                    }
                }
            ) {
        }


}
