package com.rtuitlab.assemble.domain.usecases

import com.rtuitlab.assemble.data.api.AppResult
import com.rtuitlab.assemble.data.repositores.ContainersRepository
import com.rtuitlab.assemble.domain.entities.Container

class GetContainersUseCase(
    val containersRepository: ContainersRepository
) {
    suspend operator fun invoke(room: String? = null): AppResult<List<Container>> {
        return containersRepository.getContainers(room)
    }
}