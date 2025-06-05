package com.rtuitlab.assemble.data.repositories

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.api.ContainerApi
import com.rtuitlab.assemble.data.entities.toContainerInDTO
import com.rtuitlab.assemble.domain.entities.Container
import com.rtuitlab.assemble.domain.entities.toContainer

class ContainersRepository(
    val api: ContainerApi
) {

    suspend fun getContainers(room: String? = null): RequestResult<List<Container>> {
        return api.getContainers(room).transform { list ->
            list.map { it.toContainer() }
        }
    }

    suspend fun getContainerByNumber(number: String): RequestResult<Container> {
        val result = api.getContainerByNumber(number)
        return result.transform { it.toContainer() }
    }

    suspend fun createContainer(container: Container): RequestResult<Container> {
        val result = api.createContainer(container.toContainerInDTO())
        return result.transform { it.toContainer() }
    }

    suspend fun updateContainerByNumber(
        container: Container,
        number: String
    ): RequestResult<Container> {
        val result = api.updateContainerByNumber(container.toContainerInDTO(), number)
        return result.transform { it.toContainer() }
    }
}