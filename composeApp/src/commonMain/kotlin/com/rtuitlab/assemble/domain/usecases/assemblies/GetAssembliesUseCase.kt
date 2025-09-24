package com.rtuitlab.assemble.domain.usecases.assemblies

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.repositories.AssembliesRepository
import com.rtuitlab.assemble.domain.entities.Assemble

class GetAssembliesUseCase(
    val assembliesRepository: AssembliesRepository
) {
    suspend operator fun invoke(): RequestResult<List<Assemble>> {
        return assembliesRepository.getAssemblies()
    }
}