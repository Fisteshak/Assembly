package com.rtuitlab.assemble.domain.usecases

import com.rtuitlab.assemble.data.repositores.AssembliesRepository
import com.rtuitlab.assemble.domain.entities.Component

class GetComponentsUseCase(
    val assembliesRepository: AssembliesRepository
) {
    suspend operator fun invoke(): List<Component> {
        return assembliesRepository.getComponents()
    }
}