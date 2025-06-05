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
         * gets containers from api and stores them in containers
         */
        data class GetContainers(val room: String? = null) : Intent

        /**
         * gets container by number from api and stores it in currentContainer
         */
        data class GetAndSetCurrentContainerByNumber(val number: String) : Intent

        data class SetCurrentContainer(val container: State.CurrentContainer?) : Intent

        data class SetCurrentContainerComponent(val component: Component) : Intent

        data object GetComponents : Intent

        data class CreateContainer(val container: Container) : Intent

        data class UpdateContainer(val container: Container, val number: String) : Intent

        data class SetExpectedContainerNumber(val number: String?) : Intent
    }

    data class State(
        val containers: List<Container>? = null,
        val currentContainer: CurrentContainer? = null,
        val components: List<Component> = emptyList(),
        val snackBarHostState: SnackbarHostState = SnackbarHostState(),
        val isSaving: Boolean = false,
        val expectedContainerNumber: String? = null,
    ) {
        data class CurrentContainer(
            val container: Container,
            val containerComponent: Component,
        )

    }

    sealed interface Label {
    }

}
