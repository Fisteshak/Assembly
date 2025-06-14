package com.rtuitlab.assemble.domain.usecases.containers

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.repositories.ContainersRepository

class DeleteContainerByNumberUseCase(
    val containersRepository: ContainersRepository
) {
    suspend operator fun invoke(number: String): RequestResult<Unit> {
        return containersRepository.deleteContainerByNumber(number)
    }
}