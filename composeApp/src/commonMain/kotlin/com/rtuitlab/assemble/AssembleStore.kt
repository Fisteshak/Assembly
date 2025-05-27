package com.rtuitlab.assemble

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.rtuitlab.assemble.AssembleStore.Intent
import com.rtuitlab.assemble.AssembleStore.Label
import com.rtuitlab.assemble.AssembleStore.State
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.domain.usecases.CreateAssembleUseCase
import com.rtuitlab.assemble.domain.usecases.DeleteAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GenerateSoundByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GetAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GetAssembliesUseCase
import com.rtuitlab.assemble.domain.usecases.GetComponentsUseCase
import com.rtuitlab.assemble.domain.usecases.UpdateAssembleUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


internal interface AssembleStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object FetchAssemblies : Intent
        data object FetchComponents : Intent

        data class FetchAssembleById(val id: Long) : Intent

        data class ChangeAmount(
            val assembleId: Long,
            val assembleComponentIndex: Int,
            val amount: Long
        ) : Intent


        data class SetCurrentAssemble(
            val value: Assemble?
        ) : Intent

        data class PublishAssemble(
            val value: Assemble
        ) : Intent

        data class DeleteAssembleById(
            val assembleId: Long
        ) : Intent

        /**
         * generates sound for assemble,
         * sends GeneratedSound label when completed
         */
        data class GenerateSoundById(
            val assembleId: Long
        ) : Intent
    }

    data class State(
        val currentAssemble: Assemble? = null,
        val assemblies: SnapshotStateList<Assemble> = mutableStateListOf(),
        val components: SnapshotStateList<Component> = mutableStateListOf(),
    )

    sealed interface Label {
        data class PublishedAssemble(val id: Long) : Label

        /**
         * @param assemble assemble with generated sound, or null if sound wasn't generated
         */
        data class GeneratedSound(val assemble: Assemble?) : Label

    }
}

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

        return object : AssembleStore, Store<Intent, State, Label> by storeFactory.create(
            name = "Store",
            initialState = State(),
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
        object FetchAssemblies : Action
        object FetchComponents : Action
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
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
//            println("executor got intent: $intent")

            when (intent) {
                is Intent.FetchAssemblies -> fetchAssemblies()
                is Intent.FetchAssembleById -> fetchAssembleById(intent.id)
                is Intent.FetchComponents -> fetchComponents()


                is Intent.ChangeAmount -> {
                    state().assemblies.find { it.assembleId == intent.assembleId }.also {
                        val assembleComponent =
                            it?.components!![intent.assembleComponentIndex].copy(amount = intent.amount)
                        it.components[intent.assembleComponentIndex] = assembleComponent
                    }
                }

                is Intent.SetCurrentAssemble -> {
                    dispatch(Msg.SetCurrentAssemble(intent.value))
                }

                is Intent.PublishAssemble -> {
                    scope.launch {
                        val assembleId = if (intent.value.assembleId != -1L) {
                            updateAssembleUseCase(intent.value)
                        } else {
                            createAssembleUseCase(intent.value)
                        }

                        publish(Label.PublishedAssemble(assembleId))
                    }
                }

                is Intent.DeleteAssembleById -> {
                    scope.launch {
                        deleteAssembleByIdUseCase(intent.assembleId)
                        forward(Action.FetchAssemblies)
                    }
                }


                is Intent.GenerateSoundById -> {
                    scope.launch {
                        generateSoundByIdUseCase(intent.assembleId)
                        var attempts = 0
                        val attemptsLimit = 8
                        while (attempts < attemptsLimit) {
                            attempts++
                            delay(1000)
                            val assemble = getAssembleByIdUseCase(intent.assembleId)
                            if (assemble.linkToSound != null && assemble.components != null && assemble.components.all { it.linkToSound != null }) {

                                publish(Label.GeneratedSound(assemble))
                                println("generated sound after $attempts attempts")
                                return@launch
                            }
                        }
                        publish(Label.GeneratedSound(null))
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
                val components = Msg.SetComponents(getComponentsUseCase().toMutableStateList())
                dispatch(components)
            }
        }

        private fun fetchAssembleById(id: Long) {
            scope.launch {

                val assemble = getAssembleByIdUseCase(id)
                dispatch(Msg.SetCurrentAssemble(assemble).copy())
            }
        }


    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State {


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

