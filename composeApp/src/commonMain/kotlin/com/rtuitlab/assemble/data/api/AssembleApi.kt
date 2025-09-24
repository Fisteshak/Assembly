package com.rtuitlab.assemble.data.api

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.WebClient
import com.rtuitlab.assemble.data.entities.AssembleForListOutDTO
import com.rtuitlab.assemble.data.entities.AssembleOutDTO
import com.rtuitlab.assemble.data.entities.ComponentOutDTO
import com.rtuitlab.assemble.data.entities.FullAssembleDataOutDTO
import com.rtuitlab.assemble.data.entities.toAssembleIn
import com.rtuitlab.assemble.data.entities.toAssembleUpdateIn
import com.rtuitlab.assemble.data.safeDelete
import com.rtuitlab.assemble.data.safeGet
import com.rtuitlab.assemble.data.safePatch
import com.rtuitlab.assemble.data.safePost
import com.rtuitlab.assemble.domain.entities.Assemble
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AssembleApi(
    webClient: WebClient
) {
    val client = webClient.client

    suspend fun getAssembles(): RequestResult<List<AssembleForListOutDTO>> {

        return client.safeGet("assemblies")


    }


    suspend fun getAssembleById(id: Long): RequestResult<FullAssembleDataOutDTO> {
        return client.safeGet("assemblies/$id") {}
    }

    suspend fun getComponents(): RequestResult<List<ComponentOutDTO>> {
        return client.safeGet("components") { }
    }

    suspend fun createAssemble(assemble: Assemble): RequestResult<Long> {

        val response: RequestResult<AssembleOutDTO> = client.safePost("assemblies") {
            contentType(ContentType.Application.Json)
            setBody(assemble.toAssembleIn())
        }
        println(response.toString())
        return response.transform { it.id }
    }

    suspend fun updateAssemble(assemble: Assemble): RequestResult<Long> {
        val response: RequestResult<Any> = client.safePatch("assemblies/${assemble.assembleId}") {
            contentType(ContentType.Application.Json)
            setBody(assemble.toAssembleUpdateIn())
        }
        println(response.toString())

        return response.transform { assemble.assembleId }
    }

    suspend fun deleteAssemble(assembleId: Long): RequestResult<Unit> {
        return client.safeDelete("assemblies/${assembleId}") {
        }
    }

    suspend fun generateSound(assembleId: Long): RequestResult<Unit> {
        return client.safeGet("assemblies/${assembleId}/synthesize_speech") {
        }
    }


}