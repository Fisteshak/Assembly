package com.rtuitlab.assemble.ui.container.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.rtuitlab.assemble.data.RequestError
import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.domain.entities.Container
import com.rtuitlab.assemble.domain.usecases.components.GetComponentsUseCase
import com.rtuitlab.assemble.domain.usecases.containers.CreateContainerUseCase
import com.rtuitlab.assemble.domain.usecases.containers.GetContainerByNumberUseCase
import com.rtuitlab.assemble.domain.usecases.containers.GetContainersUseCase
import com.rtuitlab.assemble.domain.usecases.containers.UpdateContainerByIdUseCase
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Intent
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Label
import com.rtuitlab.assemble.ui.container.store.ContainerStore.State
import kotlinx.coroutines.launch


internal class ContainerStoreFactory(
    private val storeFactory: StoreFactory,
    private val getContainersUseCase: GetContainersUseCase,
    private val getContainerByNumberUseCase: GetContainerByNumberUseCase,
    private val getComponentsUseCase: GetComponentsUseCase,
    private val createContainerUseCase: CreateContainerUseCase,
    private val updateContainerUseCase: UpdateContainerByIdUseCase,
) {

    private sealed interface Action {
        data class GetContainers(val room: String? = null) : Action
        data object GetComponents : Action

    }

    private sealed interface Msg {
        data class SetContainers(val containers: List<Container>) : Msg
        data class SetCurrentContainer(val container: State.CurrentContainer?) : Msg
        data class SetComponents(val components: List<Component>) : Msg

        //        data class SetContainerComponent(val component: Component) : Msg
        data class SetIsSaving(val value: Boolean) : Msg
        data class SetExpectedContainerNumber(val value: String?) : Msg
    }

    fun create(): ContainerStore =
        object : ContainerStore,
            Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
                name = "CounterStore",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Action.GetContainers(), Action.GetComponents),
                executorFactory = coroutineExecutorFactory {

                    onIntent<Intent.GetContainers> {

                        launch {
                            val result: RequestResult<List<Container>> =
                                getContainersUseCase(it.room)
                            when (result) {
                                is RequestResult.Success -> dispatch(Msg.SetContainers(result.data))
                                is RequestResult.Failure -> TODO()
                            }
                        }
                    }

                    onIntent<Intent.GetAndSetCurrentContainerByNumber> {
                        launch {

                            // set container number we are expecting to get
                            dispatch(
                                Msg.SetExpectedContainerNumber(
                                    it.number
                                )
                            )

                            val containerResult: RequestResult<Container> =
                                getContainerByNumberUseCase(it.number)

                            when (containerResult) {
                                is RequestResult.Failure -> {
                                    state().snackBarHostState.showSnackbar("Ошибка сети")
                                }

                                is RequestResult.Success -> {
                                    // if something changed and we are expecting container with another number, do nothing
                                    if (state().expectedContainerNumber == containerResult.data.number) {
//                                        dispatch(
//                                            Msg.SetCurrentContainer(
//                                                state().currentContainer?.copy(
//                                                    container = containerResult.data
//                                                )
//                                            )
//                                        )

                                        // getting components
                                        val componentsResult: RequestResult<List<Component>> =
                                            getComponentsUseCase()

                                        when (componentsResult) {
                                            is RequestResult.Success -> {
                                                // setting containerComponent
                                                dispatch(Msg.SetComponents(componentsResult.data))
                                                dispatch(
                                                    Msg.SetCurrentContainer(
                                                        State.CurrentContainer(
                                                            container = containerResult.data,
                                                            // TODO handle null-assertion
                                                            containerComponent = componentsResult.data.find { it.id == containerResult.data.componentId }!!
                                                        )
                                                    )
                                                )
                                            }

                                            is RequestResult.Failure -> TODO()
                                        }


                                    }
                                }


                            }
                        }
                    }


                    onAction<Action.GetContainers> {
                        launch {
                            val result: RequestResult<List<Container>> =
                                getContainersUseCase(it.room)
                            when (result) {
                                is RequestResult.Success -> dispatch(Msg.SetContainers(result.data))
                                is RequestResult.Failure -> TODO()
                            }
                        }
                    }

                    onIntent<Intent.SetCurrentContainer> {
                        dispatch(Msg.SetCurrentContainer(it.container))
                    }

                    onIntent<Intent.SetCurrentContainerComponent> {
                        dispatch(
                            Msg.SetCurrentContainer(
                                state().currentContainer?.copy(
                                    containerComponent = it.component
                                )
                            )
                        )

                    }

                    onIntent<Intent.GetComponents> {
                        launch {
                            val result: RequestResult<List<Component>> = getComponentsUseCase()
                            when (result) {
                                is RequestResult.Success -> dispatch(Msg.SetComponents(result.data))
                                is RequestResult.Failure -> TODO()
                            }
                        }
                    }

                    onAction<Action.GetComponents> {
                        launch {
                            val result: RequestResult<List<Component>> = getComponentsUseCase()
                            when (result) {
                                is RequestResult.Success -> dispatch(Msg.SetComponents(result.data))
                                is RequestResult.Failure -> TODO()
                            }
                        }
                    }


                    onIntent<Intent.UpdateContainer> {
                        launch {
                            dispatch(Msg.SetIsSaving(true))
                            dispatch(Msg.SetExpectedContainerNumber(it.number))

                            var result: RequestResult<Container> =
                                updateContainerUseCase(it.container, it.number)

                            dispatch(Msg.SetIsSaving(false))

                            when (result) {
                                is RequestResult.Success -> {
                                    if (state().expectedContainerNumber == result.data.number)
                                        dispatch(
                                            Msg.SetCurrentContainer(
                                                State.CurrentContainer(
                                                    result.data,
                                                    state().components.find { it.id == result.data.componentId }!!
                                                )
                                            )
                                        )



                                    state().snackBarHostState.showSnackbar(
                                        "Сохранено",
                                    )

                                }

                                is RequestResult.Failure ->
                                    when (result.error) {
                                        is RequestError.ApiError ->
                                            if (result.error.code == 409)
                                                state().snackBarHostState.showSnackbar("Ошибка сохранения. Контейнер с таким номером уже существует.")
                                            else
                                                state().snackBarHostState.showSnackbar("Ошибка сохранения.")

                                        is RequestError.NetworkError ->
                                            state().snackBarHostState.showSnackbar("Ошибка сети.")

                                        else -> state().snackBarHostState.showSnackbar("Ошибка сохранения.")

                                    }

                            }
                        }
                    }

                    onIntent<Intent.CreateContainer> {
                        launch {

                            if (it.container.number.isEmpty()) {
                                state().snackBarHostState.showSnackbar("Номер не может быть пустым.")
                                return@launch
                            }

                            if (it.container.componentId == -1L) {
                                state().snackBarHostState.showSnackbar("Компонент не может быть пустым.")
                                return@launch
                            }

                            dispatch(Msg.SetIsSaving(true))

                            var result: RequestResult<Container> =
                                createContainerUseCase(it.container)

                            dispatch(Msg.SetIsSaving(false))

                            when (result) {
                                is RequestResult.Success -> {
                                    if (state().expectedContainerNumber == null) {

                                        dispatch(
                                            Msg.SetCurrentContainer(
                                                State.CurrentContainer(
                                                    result.data,
                                                    state().components.find { it.id == result.data.componentId }!!
                                                )
                                            )
                                        )

                                        dispatch(
                                            Msg.SetExpectedContainerNumber(result.data.number)
                                        )
                                    }

                                    state().snackBarHostState.showSnackbar(
                                        "Сохранено",
                                    )

                                }

                                is RequestResult.Failure ->
                                    when (result.error) {
                                        is RequestError.ApiError ->
                                            if (result.error.code == 409)
                                                state().snackBarHostState.showSnackbar("Ошибка сохранения. Контейнер с таким номером уже существует.")
                                            else
                                                state().snackBarHostState.showSnackbar("Ошибка сохранения.")

                                        is RequestError.NetworkError ->
                                            state().snackBarHostState.showSnackbar("Ошибка сети.")

                                        else -> state().snackBarHostState.showSnackbar("Ошибка сохранения.")

                                    }

                            }


                        }
                    }

                    onIntent<Intent.SetExpectedContainerNumber> {
                        dispatch(Msg.SetExpectedContainerNumber(it.number))
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

                        is Msg.SetComponents -> copy(components = msg.components)

                        is Msg.SetIsSaving -> copy(isSaving = msg.value)

                        is Msg.SetExpectedContainerNumber -> copy(expectedContainerNumber = msg.value)
                    }
                }
            ) {
        }


}
