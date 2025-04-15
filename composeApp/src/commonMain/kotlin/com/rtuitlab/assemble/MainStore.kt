package com.rtuitlab.assemble

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.rtuitlab.assemble.MainStore.Intent
import com.rtuitlab.assemble.MainStore.State

internal interface MainStore : Store<Intent, State, Nothing> {

    sealed interface Intent {
        data class LogoExpanded(val value: Boolean) : Intent

    }

    data class State(val expanded: Boolean = false)

    sealed interface Label {
    }
}

internal class MainStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): MainStore {

        return object : MainStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "Store",
            initialState = State(),
            reducer = ReducerImpl,
            executorFactory = MainStoreFactory::ExecutorImpl
        ) {}

    }

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class Expanded(val value: Boolean) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Nothing, State, Msg, Nothing>() {
        override fun executeIntent(intent: Intent) {
            println("executor got intent: $intent")
            when (intent) {
                is Intent.LogoExpanded -> dispatch(Msg.Expanded(intent.value))
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
            }
        }
    }
}
