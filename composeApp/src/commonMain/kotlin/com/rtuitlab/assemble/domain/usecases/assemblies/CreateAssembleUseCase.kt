package com.rtuitlab.assemble.domain.usecases.assemblies

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.repositories.AssembliesRepository
import com.rtuitlab.assemble.domain.entities.Assemble

class CreateAssembleUseCase(
    val assembliesRepository: AssembliesRepository
) {
    /**
     * returns ID
     */
    suspend operator fun invoke(assemble: Assemble): RequestResult<Long> {
        return assembliesRepository.createAssemble(assemble)
    }
}