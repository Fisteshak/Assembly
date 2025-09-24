package com.rtuitlab.assemble.domain.usecases

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.repositories.AssembliesRepository

class GenerateSoundByIdUseCase(
    val assembliesRepository: AssembliesRepository
) {
    suspend operator fun invoke(assembleId: Long): RequestResult<Unit> {
        return assembliesRepository.generateSound(assembleId)
    }
}