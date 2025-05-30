package com.rtuitlab.assemble.data.repositores

import com.rtuitlab.assemble.data.api.AppResult
import com.rtuitlab.assemble.data.api.ContainerApi
import com.rtuitlab.assemble.domain.entities.Container
import com.rtuitlab.assemble.domain.entities.toContainer

class ContainersRepository(
    val api: ContainerApi
) {
    suspend fun getContainers(room: String? = null): AppResult<List<Container>> {
        return api.getContainers(room).transform { list ->
            list.map { it.toContainer() }
        }
    }

    suspend fun getContainerByNumber(number: String): AppResult<Container> {
        val result = api.getContainerByNumber(number)
        return result.transform { it.toContainer() }
    }
}