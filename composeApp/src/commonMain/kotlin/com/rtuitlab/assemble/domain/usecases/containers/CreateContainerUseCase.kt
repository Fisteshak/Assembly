package com.rtuitlab.assemble.domain.usecases.containers

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.repositories.ContainersRepository
import com.rtuitlab.assemble.domain.entities.Container

class CreateContainerUseCase(
    val containersRepository: ContainersRepository
) {
    suspend operator fun invoke(container: Container): RequestResult<Container> {
        return containersRepository.createContainer(container)
    }
}