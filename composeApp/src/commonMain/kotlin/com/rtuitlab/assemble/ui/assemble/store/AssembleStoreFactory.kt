package com.rtuitlab.assemble.ui.assemble.store

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.domain.usecases.GenerateSoundByIdUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.CreateAssembleUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.DeleteAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.GetAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.GetAssembliesUseCase
import com.rtuitlab.assemble.domain.usecases.assemblies.UpdateAssembleUseCase
import com.rtuitlab.assemble.domain.usecases.components.GetComponentsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class AssembleStoreFactory(
    private val storeFactory: StoreFactory,
    private val getAssembliesUseCase: GetAssembliesUseCase,
    private val getAssembleByIdUseCase: GetAssembleByIdUseCase,
    private val getComponentsUseCase: GetComponentsUseCase,
    private val createAssembleUseCase: CreateAssembleUseCase,
    private val updateAssembleUseCase: UpdateAssembleUseCase,
    private val deleteAssembleByIdUseCase: DeleteAssembleByIdUseCase,
    private val generateSoundByIdUseCase: GenerateSoundByIdUseCase,
) {


    fun create(): AssembleStore {

        return object : AssembleStore,
            Store<AssembleStore.Intent, AssembleStore.State, AssembleStore.Label> by storeFactory.create(
                name = "Store",
                initialState = AssembleStore.State(),
                reducer = ReducerImpl,
                bootstrapper = SimpleBootstrapper(Action.FetchAssemblies, Action.FetchComponents),
                executorFactory = {
                    ExecutorImpl(
                        getAssembliesUseCase,
                        getAssembleByIdUseCase,
                        getComponentsUseCase,
                        createAssembleUseCase,
                        updateAssembleUseCase,
                        deleteAssembleByIdUseCase,
                        generateSoundByIdUseCase
                    )
                }
            ) {}

    }

    private sealed interface Action {
        data object FetchAssemblies : Action
        data object FetchComponents : Action
    }

    private sealed interface Msg {
        data class SetAssemblies(val value: SnapshotStateList<Assemble>) : Msg
        data class SetAssemble(val value: Assemble) : Msg
        data class SetComponents(val value: SnapshotStateList<Component>) : Msg
        data class SetCurrentAssemble(val value: Assemble?) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }

    private class ExecutorImpl(
        val getAssembliesUseCase: GetAssembliesUseCase,
        val getAssembleByIdUseCase: GetAssembleByIdUseCase,
        val getComponentsUseCase: GetComponentsUseCase,
        val createAssembleUseCase: CreateAssembleUseCase,
        val updateAssembleUseCase: UpdateAssembleUseCase,
        val deleteAssembleByIdUseCase: DeleteAssembleByIdUseCase,
        val generateSoundByIdUseCase: GenerateSoundByIdUseCase,
    ) : CoroutineExecutor<AssembleStore.Intent, Action, AssembleStore.State, Msg, AssembleStore.Label>() {

        var currentJob: Job? = null

        override fun executeIntent(intent: AssembleStore.Intent) {
            when (intent) {
                is AssembleStore.Intent.FetchAssemblies -> fetchAssemblies()
                is AssembleStore.Intent.FetchAssembleById -> fetchAssembleById(intent.id)
                is AssembleStore.Intent.FetchComponents -> fetchComponents()


                is AssembleStore.Intent.ChangeAmount -> {
                    state().assemblies.find { it.assembleId == intent.assembleId }.also {
                        val assembleComponent =
                            it?.components!![intent.assembleComponentIndex].copy(amount = intent.amount)
                        it.components[intent.assembleComponentIndex] = assembleComponent
                    }
                }

                is AssembleStore.Intent.SetCurrentAssemble -> {
                    currentJob?.cancel()
                    dispatch(Msg.SetCurrentAssemble(intent.value))
                }

                is AssembleStore.Intent.PublishAssemble -> {
                    currentJob = scope.launch {
                        val assembleId = if (intent.value.assembleId != -1L) {
                            updateAssembleUseCase(intent.value)
                        } else {
                            createAssembleUseCase(intent.value)
                        }

                        publish(AssembleStore.Label.PublishedAssemble(assembleId))
                    }
                }

                is AssembleStore.Intent.DeleteAssembleById -> {
                    scope.launch {
                        deleteAssembleByIdUseCase(intent.assembleId)
                        forward(Action.FetchAssemblies)
                    }
                }


                is AssembleStore.Intent.GenerateSoundById -> {
                    scope.launch {
                        generateSoundByIdUseCase(intent.assembleId)
                        var attempts = 0
                        val attemptsLimit = 8
                        while (attempts < attemptsLimit) {
                            attempts++
                            delay(1000)
                            val assemble = getAssembleByIdUseCase(intent.assembleId)
                            if (assemble.linkToSound != null && assemble.components != null && assemble.components.all { it.linkToSound != null }) {

                                publish(AssembleStore.Label.GeneratedSound(assemble))
                                println("generated sound after $attempts attempts")
                                return@launch
                            }
                        }
                        publish(AssembleStore.Label.GeneratedSound(null))
                        println("failed to generate sound after $attemptsLimit attempts")

                    }
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.FetchAssemblies -> fetchAssemblies()
                Action.FetchComponents -> fetchComponents()
            }
        }

        private fun fetchAssemblies() {
            scope.launch {
                val assemblies = getAssembliesUseCase().toMutableStateList()
                dispatch(Msg.SetAssemblies(assemblies))
            }
        }

        private fun fetchComponents() {
            scope.launch {
                val result: RequestResult<List<Component>> = getComponentsUseCase()
                when (result) {
                    is RequestResult.Success -> {
                        dispatch(
                            Msg.SetComponents(result.data.toMutableStateList())
                        )
                    }

                    is RequestResult.Failure -> TODO()
                }
            }
        }

        private fun fetchAssembleById(id: Long) {

            currentJob?.cancel()
            currentJob = scope.launch {
                val assemble = getAssembleByIdUseCase(id)
                dispatch(Msg.SetCurrentAssemble(assemble).copy())
            }
        }


    }

    private object ReducerImpl : Reducer<AssembleStore.State, Msg> {
        override fun AssembleStore.State.reduce(msg: Msg): AssembleStore.State {


            return when (msg) {

                is Msg.SetAssemblies -> {
                    copy(assemblies = msg.value)
                }

                is Msg.SetComponents -> {
                    println(msg.value)
                    copy(components = msg.value)
                }

                is Msg.SetAssemble -> {
                    val new = assemblies.toMutableList()
                    val index = new.indexOfFirst { msg.value.assembleId == it.assembleId }
                    new[index] = msg.value
                    copy(assemblies = new.toMutableStateList())
                }

                is Msg.SetCurrentAssemble -> {

                    val assemble = msg.value
                    copy(currentAssemble = assemble?.copy(components = assemble.components?.toMutableStateList()))
                }
            }
        }
    }
}