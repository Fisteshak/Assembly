package com.rtuitlab.assemble.domain.usecases.assemblies

import com.rtuitlab.assemble.data.repositories.AssembliesRepository
import com.rtuitlab.assemble.domain.entities.Assemble

class UpdateAssembleUseCase(
    val assembliesRepository: AssembliesRepository
) {
    /**
     * returns ID
     */
    suspend operator fun invoke(assemble: Assemble): Long {
        return assembliesRepository.updateAssemble(assemble)
    }
}