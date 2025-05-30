package com.rtuitlab.assemble.ui.container.store

import com.arkivanov.mvikotlin.core.store.Store
import com.rtuitlab.assemble.domain.entities.Container
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Intent
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Label
import com.rtuitlab.assemble.ui.container.store.ContainerStore.State

internal interface ContainerStore : Store<Intent, State, Label> {

    sealed interface Intent {
        /**
         * gets containers from api and stores them in containers
         */
        data class GetContainers(val room: String? = null) : Intent

        /**
         * gets container by number from api and stores it in currentContainer
         */
        data class GetAndSetCurrentContainerByNumber(val number: String) : Intent

        data class SetCurrentContainer(val container: State.CurrentContainer?) : Intent
    }

    data class State(
        val containers: List<Container>? = null,
        val currentContainer: CurrentContainer? = null
    ) {

        data class CurrentContainer(
            val expectedNumber: String?,
            val container: Container?
        )
    }

    sealed interface Label {
//        data class ErrorMessage(val message: String) : Label
    }

}
