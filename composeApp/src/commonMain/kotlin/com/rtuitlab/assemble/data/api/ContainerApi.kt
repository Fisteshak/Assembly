package com.rtuitlab.assemble.data.api

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.WebClient
import com.rtuitlab.assemble.data.entities.ContainerInDTO
import com.rtuitlab.assemble.data.entities.ContainerOutDTO
import com.rtuitlab.assemble.data.safeDelete
import com.rtuitlab.assemble.data.safeGet
import com.rtuitlab.assemble.data.safePatch
import com.rtuitlab.assemble.data.safePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ContainerApi(
    val webClient: WebClient
) {
    val client = webClient.client

    suspend fun getContainers(room: String? = null): RequestResult<List<ContainerOutDTO>> {
        return client.safeGet("containers") {
            if (room != null) url.parameters.append("room", room)
        }
    }

    suspend fun getContainerByNumber(number: String): RequestResult<ContainerOutDTO> {
        return client.safeGet("containers/$number")
    }

    suspend fun deleteContainerByNumber(number: String): RequestResult<Unit> {
        return client.safeDelete("containers/$number")
    }

    suspend fun createContainer(container: ContainerInDTO): RequestResult<ContainerOutDTO> {
        return client.safePost("containers") {
            contentType(ContentType.Application.Json)
            setBody(container)
        }
    }

    suspend fun updateContainerByNumber(
        container: ContainerInDTO,
        number: String
    ): RequestResult<ContainerOutDTO> {
        return client.safePatch("containers/${number}") {
            contentType(ContentType.Application.Json)
            setBody(container)
        }
    }

}