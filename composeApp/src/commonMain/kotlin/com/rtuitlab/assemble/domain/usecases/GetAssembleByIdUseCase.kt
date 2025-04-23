package com.rtuitlab.assemble.domain.usecases

import com.rtuitlab.assemble.data.repositores.AssembliesRepository
import com.rtuitlab.assemble.domain.entities.Assemble

class GetAssembleByIdUseCase(
    val assembliesRepository: AssembliesRepository
) {
    suspend operator fun invoke(id: Long): Assemble {
        return assembliesRepository.getAssembleById(id)
    }
}