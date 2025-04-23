package com.rtuitlab.assemble.domain.usecases

import com.rtuitlab.assemble.data.repositores.AssembliesRepository
import com.rtuitlab.assemble.domain.entities.Assemble

class GetAssembliesUseCase(
    val assembliesRepository: AssembliesRepository
) {
    suspend operator fun invoke(): List<Assemble> {
        return assembliesRepository.getAssemblies()
    }
}