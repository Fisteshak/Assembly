package com.rtuitlab.assemble.data.api

import com.rtuitlab.assemble.data.entities.ContainerOutDTO

class ContainerApi(
    val webClient: WebClient
) {
    val client = webClient.client

    suspend fun getContainers(room: String? = null): AppResult<List<ContainerOutDTO>> {
        return client.safeGet("containers") {
            if (room != null) url.parameters.append("room", room)
        }
    }

    suspend fun getContainerByNumber(number: String): AppResult<ContainerOutDTO> {
        return client.safeGet("containers/$number")
    }
}