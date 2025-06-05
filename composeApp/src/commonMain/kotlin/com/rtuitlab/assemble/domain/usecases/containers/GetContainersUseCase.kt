package com.rtuitlab.assemble.domain.usecases.containers

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.repositories.ContainersRepository
import com.rtuitlab.assemble.domain.entities.Container

class GetContainersUseCase(
    val containersRepository: ContainersRepository
) {
    suspend operator fun invoke(room: String? = null): RequestResult<List<Container>> {
        return containersRepository.getContainers(room)
    }
}