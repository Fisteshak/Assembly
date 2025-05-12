package com.rtuitlab.assemble.data.repositores

import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.domain.entities.toAssemble
import com.rtuitlab.assemble.domain.entities.toComponent

class AssembliesRepository(
    val api: AssembleApi
) {

    suspend fun getAssemblies(): List<Assemble> {
        return api.getAssembles().map {
            it.toAssemble()
        }
    }

    suspend fun getAssembleById(id: Long): Assemble {
        return api.getAssembleById(id).toAssemble()
    }

    suspend fun getComponents(): List<Component> {
        return api.getComponents().map { it.toComponent() }
    }

    /**
     * returns ID
     */
    suspend fun createAssemble(assemble: Assemble): Long {
        return api.createAssemble(assemble)
    }

    /**
     * returns ID
     */
    suspend fun updateAssemble(assemble: Assemble): Long {
        return api.updateAssemble(assemble)
    }

    suspend fun deleteAssemble(assembleId: Long) {
        return api.deleteAssemble(assembleId)
    }

    suspend fun generateSound(assembleId: Long) {
        return api.generateSound(assembleId)
    }


}