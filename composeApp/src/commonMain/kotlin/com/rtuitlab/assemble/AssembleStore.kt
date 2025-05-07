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
import com.rtuitlab.assemble.AssembleStore.State
import com.rtuitlab.assemble.AssembleStoreFactory.Msg.Assemblies
import com.rtuitlab.assemble.AssembleStoreFactory.Msg.Components
import com.rtuitlab.assemble.AssembleStoreFactory.Msg.Expanded
import com.rtuitlab.assemble.AssembleStoreFactory.Msg.UpdateAssemble
import com.rtuitlab.assemble.AssembleStoreFactory.Msg.UpdateCurrentAssemble
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.domain.usecases.CreateAssembleUseCase
import com.rtuitlab.assemble.domain.usecases.DeleteAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GetAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GetAssembliesUseCase
import com.rtuitlab.assemble.domain.usecases.GetComponentsUseCase
import com.rtuitlab.assemble.domain.usecases.UpdateAssembleUseCase
import kotlinx.coroutines.launch


internal interface AssembleStore : Store<Intent, State, Nothing> {

    sealed interface Intent {
        data class LogoExpanded(val value: Boolean) : Intent
        object FetchAssemblies : Intent
        data class FetchAssembleById(val id: Long) : Intent
        object FetchComponents : Intent
        data class AssembleComponentSelectionState(
            val assembleId: Long,
            val assembleComponentIndex: Int,
            val name: String
        ) : Intent

        data class ChangeAmount(
            val assembleId: Long,
            val assembleComponentIndex: Int,
            val amount: Long
        ) : Intent

        data class UpdateCurrentAssemble(
            val value: Assemble?
        ) : Intent

        data class PublishAssemble(
            val value: Assemble
        ) : Intent

        data class DeleteAssemble(
            val assembleId: Long
        ) : Intent
    }

    data class State(
        val expanded: Boolean = false,
        val assemblies: SnapshotStateList<Assemble> = mutableStateListOf(),
        val components: SnapshotStateList<Component> = mutableStateListOf(),
        val currentAssemble: Assemble? = null
    )

    sealed interface Label {
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
) {

    fun create(): AssembleStore {

        return object : AssembleStore, Store<Intent, State, Nothing> by storeFactory.create(
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
                    deleteAssembleByIdUseCase
                )
            }
        ) {}

    }

    private sealed interface Action {
        object FetchAssemblies : Action
        object FetchComponents : Action
    }

    private sealed interface Msg {
        data class Expanded(val value: Boolean) : Msg
        data class Assemblies(val value: SnapshotStateList<Assemble>) : Msg
        data class UpdateAssemble(val value: Assemble) : Msg
        data class Components(val value: SnapshotStateList<Component>) : Msg
        data class UpdateCurrentAssemble(val value: Assemble?) : Msg
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
    ) : CoroutineExecutor<Intent, Action, State, Msg, Nothing>() {
        override fun executeIntent(intent: Intent) {
//            println("executor got intent: $intent")

            when (intent) {
                is Intent.LogoExpanded -> dispatch(Expanded(intent.value))
                is Intent.FetchAssemblies -> fetchAssemblies()
                is Intent.FetchAssembleById -> fetchAssembleById(intent.id)
                is Intent.FetchComponents -> fetchComponents()
                is Intent.AssembleComponentSelectionState -> {

                }

                is Intent.ChangeAmount -> {
                    state().assemblies.find { it.assembleId == intent.assembleId }.also {
                        val assembleComponent =
                            it?.components!![intent.assembleComponentIndex].copy(amount = intent.amount)
                        it.components[intent.assembleComponentIndex] = assembleComponent
                    }
                }

                is Intent.UpdateCurrentAssemble -> {
                    dispatch(UpdateCurrentAssemble(intent.value))
                }

                is Intent.PublishAssemble -> {
                    scope.launch {

                        if (intent.value.assembleId != -1L) {
                            updateAssembleUseCase(intent.value)
                        } else {
                            createAssembleUseCase(intent.value)

                        }
                        forward(Action.FetchAssemblies)
                    }
                }

                is Intent.DeleteAssemble -> {
                    scope.launch {
                        deleteAssembleByIdUseCase(intent.assembleId)
                        forward(Action.FetchAssemblies)

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
                dispatch(Msg.Assemblies(assemblies))
            }
        }

        private fun fetchComponents() {
            scope.launch {
                val components = Msg.Components(getComponentsUseCase().toMutableStateList())
                dispatch(components)
            }
        }

        private fun fetchAssembleById(id: Long) {
            scope.launch {
                val assemble = getAssembleByIdUseCase(id)
//                dispatch(Msg.UpdateAssemble(assemble))
                dispatch(Msg.UpdateCurrentAssemble(assemble).copy())
            }
        }


    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State {


//            println("reducer got message: $msg")
            return when (msg) {
                is Expanded -> {
                    copy(expanded = msg.value)
                }

                is Assemblies -> {
                    copy(assemblies = msg.value)
                }

                is Components -> {
                    println(msg.value)
                    copy(components = msg.value)
                }

                is UpdateAssemble -> {
                    val new = assemblies.toMutableList()
                    val index = new.indexOfFirst { msg.value.assembleId == it.assembleId }
                    new[index] = msg.value
                    copy(assemblies = new.toMutableStateList())
                }

                is Msg.UpdateCurrentAssemble ->
                    copy(currentAssemble = msg.value)
            }
        }
    }
}

