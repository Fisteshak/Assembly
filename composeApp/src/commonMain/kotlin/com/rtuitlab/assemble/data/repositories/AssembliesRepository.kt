package com.rtuitlab.assemble.data.repositories

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.api.AssembleApi
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.domain.entities.toAssemble
import com.rtuitlab.assemble.domain.entities.toComponent

class AssembliesRepository(
    val api: AssembleApi
) {

    suspend fun getAssemblies(): RequestResult<List<Assemble>> {
        return api.getAssembles().transform { body ->
            body.map { it.toAssemble() }
        }
    }

    suspend fun getAssembleById(id: Long): RequestResult<Assemble> {
        return api.getAssembleById(id).transform { it.toAssemble() }
    }

    suspend fun getComponents(): RequestResult<List<Component>> {
        return api.getComponents().transform { body ->
            body.map { it.toComponent() }
        }
    }

    /**
     * returns ID
     */
    suspend fun createAssemble(assemble: Assemble): RequestResult<Long> {
        return api.createAssemble(assemble)
    }

    /**
     * returns ID
     */
    suspend fun updateAssemble(assemble: Assemble): RequestResult<Long> {
        return api.updateAssemble(assemble)
    }

    suspend fun deleteAssemble(assembleId: Long): RequestResult<Unit> {
        return api.deleteAssemble(assembleId)
    }

    suspend fun generateSound(assembleId: Long): RequestResult<Unit> {
        return api.generateSound(assembleId)
    }


}