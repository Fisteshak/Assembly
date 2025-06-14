package com.rtuitlab.assemble.ui.container.store

import androidx.compose.material.SnackbarHostState
import com.arkivanov.mvikotlin.core.store.Store
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.domain.entities.Container
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Intent
import com.rtuitlab.assemble.ui.container.store.ContainerStore.Label
import com.rtuitlab.assemble.ui.container.store.ContainerStore.State

internal interface ContainerStore : Store<Intent, State, Label> {

    sealed interface Intent {
        /**
         * gets containers from api and stores them in containers. Cancels currentJob if it exists
         */
        data class GetContainers(val room: String? = null) : Intent

        /**
         * gets container by number from api and stores it in currentContainer. Cancels currentJob if it exists
         */
        data class GetCurrentContainerByNumber(val number: String) : Intent

        /**
         * sets currentContainer
         */
        data class SetCurrentContainer(val container: State.CurrentContainer?) : Intent

        data class SetCurrentContainerComponent(val component: Component) : Intent

        data object GetComponents : Intent

        /**
         * creates new container using api and assigns it to currentContainer. Cancels currentJob if it exists
         */
        data class CreateCurrentContainer(val container: Container) : Intent

        /**
         * updates container by number using api and assigns it to currentContainer. Cancels currentJob if it exists
         */
        data class UpdateCurrentContainerByNumber(val container: Container, val number: String) :
            Intent

        /**
         * assigns new empty container to currentContainer. Cancels currentJob if it exists
         */
        data object SetNewCurrentContainer : Intent

        data class DeleteContainerByNumber(val number: String) : Intent

    }

    data class State(
        val containers: List<Container>? = null,
        val currentContainer: CurrentContainer? = null,
        val components: List<Component> = emptyList(),
        val snackBarHostState: SnackbarHostState = SnackbarHostState(),
        val isSaving: Boolean = false,
    ) {
        data class CurrentContainer(
            val container: Container,
            val containerComponent: Component,
            val number: String? // user can change id (number), so this is needed to remember original one. If container is new, than number is null
        )

    }

    sealed interface Label {
    }

}
