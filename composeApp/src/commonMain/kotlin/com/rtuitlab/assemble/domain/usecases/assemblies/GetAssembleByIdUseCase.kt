package com.rtuitlab.assemble.domain.usecases.assemblies

import com.rtuitlab.assemble.data.repositories.AssembliesRepository
import com.rtuitlab.assemble.domain.entities.Assemble

class GetAssembleByIdUseCase(
    val assembliesRepository: AssembliesRepository
) {
    suspend operator fun invoke(id: Long): Assemble {
        return assembliesRepository.getAssembleById(id).also { println(it) }
    }
}