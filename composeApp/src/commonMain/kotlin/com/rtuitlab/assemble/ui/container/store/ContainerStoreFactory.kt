package com.rtuitlab.assemble.ui.container.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.rtuitlab.assemble.data.RequestError
import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.qr.QrPdf
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.domain.entities.Container
import com.rtuitlab.assemble.domain.usecases.components.GetComponentsUseCase
import com.rtuitlab.assemble.domain.usecases.containers.CreateContainerUseCase
import com.rtuitlab.assemble.domain.usecases.containers.DeleteContainerByNumberUseCase
import com.rtuitlab.assemble.domain.usecases.containers.GetContainerByNumberUseCase
import com.rtuitlab.assemble.domain.usecases.containers.GetContainersUseCase
import com.rtuitlab.assemble.domain.usecases.containers.UpdateContainerByIdUseCase
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Intent
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Label
import com.rtuitlab.assemble.ui.container.store.ContainerStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


internal class ContainerStoreFactory(
    private val storeFactory: StoreFactory,
    private val getContainersUseCase: GetContainersUseCase,
    private val getContainerByNumberUseCase: GetContainerByNumberUseCase,
    private val getComponentsUseCase: GetComponentsUseCase,
    private val createContainerUseCase: CreateContainerUseCase,
    private val updateContainerUseCase: UpdateContainerByIdUseCase,
    private val deleteContainerByNumberUseCase: DeleteContainerByNumberUseCase,
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
    }

    fun create(): ContainerStore =
        object : ContainerStore,
            Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
                name = "CounterStore",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Action.GetContainers(), Action.GetComponents),
                executorFactory = coroutineExecutorFactory {
                    // any coroutine that changes currentContainer (for example GetCurrentContainerByNumber) should be stored here.
                    // If another such coroutine is created, it should cancel or join existing one
                    // so at any point there may be only one suspending job that will change currentContainer (like getting, saving, etc)
                    var currentJob: Job? = null

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

                    onIntent<Intent.GetCurrentContainerByNumber> {

                        currentJob?.cancel()

                        currentJob = launch {

                            val containerResult: RequestResult<Container> =
                                getContainerByNumberUseCase(it.number)


                            when (containerResult) {
                                is RequestResult.Failure -> {
                                    state().snackBarHostState.showSnackbar("Ошибка сети")
                                }

                                is RequestResult.Success -> {

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
                                                        containerComponent = componentsResult.data.find { it.id == containerResult.data.componentId }!!,
                                                        number = containerResult.data.number
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


                    onIntent<Intent.UpdateCurrentContainerByNumber> {

                        currentJob?.cancel()

                        currentJob = launch {

                            if (it.container.number.isEmpty()) {
                                state().snackBarHostState.showSnackbar("Номер не может быть пустым.")
                                return@launch
                            }

                            if (it.container.componentId == -1L) {
                                state().snackBarHostState.showSnackbar("Добавьте деталь.")
                                return@launch
                            }



                            dispatch(Msg.SetIsSaving(true))

                            var result: RequestResult<Container> =
                                updateContainerUseCase(it.container, it.number)

                            dispatch(Msg.SetIsSaving(false))

                            when (result) {
                                is RequestResult.Success -> {
                                    dispatch(
                                        Msg.SetCurrentContainer(
                                            State.CurrentContainer(
                                                result.data,
                                                state().components.find { it.id == result.data.componentId }!!,
                                                number = result.data.number
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

                    onIntent<Intent.CreateCurrentContainer> {
                        currentJob?.cancel()
                        currentJob = launch {

                            if (it.container.number.isEmpty()) {
                                state().snackBarHostState.showSnackbar("Номер не может быть пустым.")
                                return@launch
                            }

                            if (it.container.componentId == -1L) {
                                state().snackBarHostState.showSnackbar("Добавьте деталь.")
                                return@launch
                            }

                            dispatch(Msg.SetIsSaving(true))

                            var result: RequestResult<Container> =
                                createContainerUseCase(it.container)

                            dispatch(Msg.SetIsSaving(false))

                            when (result) {
                                is RequestResult.Success -> {

                                        dispatch(
                                            Msg.SetCurrentContainer(
                                                State.CurrentContainer(
                                                    result.data,
                                                    state().components.find { it.id == result.data.componentId }!!,
                                                    number = result.data.number
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

                    onIntent<Intent.SetNewCurrentContainer> {
                        currentJob?.cancel()

                        dispatch(
                            Msg.SetCurrentContainer(
                                State.CurrentContainer(
                                    Container("", "", 1, -1L), Component.createEmpty(), null
                                )
                            )
                        )
                    }

                    onIntent<Intent.DeleteContainerByNumber> {
                        launch {
                            deleteContainerByNumberUseCase(it.number)
                            forward(Action.GetContainers())
                        }
                    }

                    onIntent<Intent.Print> { intent ->
                        launch {
                            with(QrPdf(cols = 3)) {
                                repeat(40) {
                                    addQr(intent.pngImage.toByteArray(), intent.name)
                                }
                                print()
                            }
//                            pdfPrinter.print(it.pngImage.toByteArray())

                        }
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

                    }
                }
            ) {
        }


}
