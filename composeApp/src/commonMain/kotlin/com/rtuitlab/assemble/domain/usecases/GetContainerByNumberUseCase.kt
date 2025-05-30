package com.rtuitlab.assemble.domain.usecases

import com.rtuitlab.assemble.data.api.AppResult
import com.rtuitlab.assemble.data.repositores.ContainersRepository
import com.rtuitlab.assemble.domain.entities.Container

class GetContainerByNumberUseCase(
    val containersRepository: ContainersRepository
) {
    suspend operator fun invoke(number: String): AppResult<Container> {
        return containersRepository.getContainerByNumber(number)
    }
}