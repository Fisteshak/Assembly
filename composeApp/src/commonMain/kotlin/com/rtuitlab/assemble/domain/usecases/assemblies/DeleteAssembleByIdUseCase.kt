package com.rtuitlab.assemble.domain.usecases.assemblies

import com.rtuitlab.assemble.data.repositories.AssembliesRepository

class DeleteAssembleByIdUseCase(
    val assembliesRepository: AssembliesRepository
) {
    suspend operator fun invoke(assembleId: Long) {
        return assembliesRepository.deleteAssemble(assembleId)
    }
}