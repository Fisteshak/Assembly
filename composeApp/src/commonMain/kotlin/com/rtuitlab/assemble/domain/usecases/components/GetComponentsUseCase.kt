package com.rtuitlab.assemble.domain.usecases.components

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.repositories.ComponentsRepository
import com.rtuitlab.assemble.domain.entities.Component

class GetComponentsUseCase(
    val componentsRepository: ComponentsRepository
) {
    suspend operator fun invoke(): RequestResult<List<Component>> {
        return componentsRepository.getComponents()
    }
}