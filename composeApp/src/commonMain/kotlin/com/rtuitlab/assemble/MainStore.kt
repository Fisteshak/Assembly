package com.rtuitlab.assemble

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.rtuitlab.assemble.MainStore.Intent
import com.rtuitlab.assemble.MainStore.State
import com.rtuitlab.assemble.data.repositores.AssembleApi
import com.rtuitlab.assemble.data.repositores.AssembliesRepository
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.domain.usecases.GetAssembleByIdUseCase
import com.rtuitlab.assemble.domain.usecases.GetAssembliesUseCase
import kotlinx.coroutines.launch

internal interface MainStore : Store<Intent, State, Nothing> {

    sealed interface Intent {
        data class LogoExpanded(val value: Boolean) : Intent
        object FetchAssemblies : Intent
        data class FetchAssembleById(val id: Long) : Intent
    }

    data class State(
        val expanded: Boolean = false,
        val assemblies: MutableList<Assemble> = mutableListOf(),
    )

    sealed interface Label {
    }
}

internal class MainStoreFactory(
    private val storeFactory: StoreFactory,

    ) {

    fun create(): MainStore {

        return object : MainStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "Store",
            initialState = State(),
            reducer = ReducerImpl,
            bootstrapper = SimpleBootstrapper(Action.FetchAssemblies),
            executorFactory = MainStoreFactory::ExecutorImpl
        ) {}

    }

    private sealed interface Action {
        object FetchAssemblies : Action

    }

    private sealed interface Msg {
        data class Expanded(val value: Boolean) : Msg
        data class Assemblies(val value: MutableList<Assemble>) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }

    private class ExecutorImpl(
        val getAssembliesUseCase: GetAssembliesUseCase = GetAssembliesUseCase(
            AssembliesRepository(
                AssembleApi()
            )
        ),
        val getAssembleByIdUseCase: GetAssembleByIdUseCase = GetAssembleByIdUseCase(
            AssembliesRepository(AssembleApi())
        ),
    ) : CoroutineExecutor<Intent, Action, State, Msg, Nothing>() {
        override fun executeIntent(intent: Intent) {
            println("executor got intent: $intent")
            when (intent) {
                is Intent.LogoExpanded -> dispatch(Msg.Expanded(intent.value))
                is Intent.FetchAssemblies -> fetchAssemblies()
                is Intent.FetchAssembleById -> fetchAssembleById(intent.id)
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.FetchAssemblies -> fetchAssemblies()
            }
        }

        private fun fetchAssemblies() {
            scope.launch {
                val assemblies = Msg.Assemblies(getAssembliesUseCase() as MutableList<Assemble>)
                dispatch(assemblies)
            }
        }

        private fun fetchAssembleById(id: Long) {
            scope.launch {
                val assemble = getAssembleByIdUseCase(id)
                state().assemblies[id.toInt()] = assemble

                dispatch(Msg.Assemblies(state().assemblies))
            }
        }

    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State {


            println("reducer got message: $msg")
            return when (msg) {
                is Msg.Expanded -> {
                    copy(expanded = msg.value)
                }

                is Msg.Assemblies -> {
                    copy(assemblies = msg.value)
                }
            }
        }
    }
}
