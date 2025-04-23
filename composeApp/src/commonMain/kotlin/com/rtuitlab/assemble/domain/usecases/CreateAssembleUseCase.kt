package com.rtuitlab.assemble.domain.usecases

import com.rtuitlab.assemble.data.repositores.AssembliesRepository
import com.rtuitlab.assemble.domain.entities.Assemble

class CreateAssembleUseCase(
    val assembliesRepository: AssembliesRepository
) {
    suspend operator fun invoke(assemble: Assemble): Assemble {
        return assembliesRepository.createAssemble(assemble)
    }
}