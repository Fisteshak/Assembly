package com.rtuitlab.assemble.ui.assemble.store

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.arkivanov.mvikotlin.core.store.Store
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.ui.assemble.store.AssembleStore.Intent
import com.rtuitlab.assemble.ui.assemble.store.AssembleStore.Label
import com.rtuitlab.assemble.ui.assemble.store.AssembleStore.State


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
        val snackBarHostState: SnackbarHostState = SnackbarHostState(),
        val isSaving: Boolean = false
    )

    sealed interface Label {
        data class PublishedAssemble(val id: Long) : Label

        /**
         * @param assemble assemble with generated sound, or null if sound wasn't generated
         */
        data class GeneratedSound(val assemble: Assemble?) : Label

    }
}

