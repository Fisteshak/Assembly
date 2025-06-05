package com.rtuitlab.assemble.domain.usecases.containers

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.repositories.ContainersRepository
import com.rtuitlab.assemble.domain.entities.Container

class UpdateContainerByIdUseCase(
    val containersRepository: ContainersRepository
) {
    suspend operator fun invoke(container: Container, number: String): RequestResult<Container> {
        return containersRepository.updateContainerByNumber(container, number)
    }
}