package com.rtuitlab.assemble.data.api

import com.rtuitlab.assemble.data.WebClient
import com.rtuitlab.assemble.data.entities.AssembleForListOutDTO
import com.rtuitlab.assemble.data.entities.AssembleOutDTO
import com.rtuitlab.assemble.data.entities.ComponentOutDTO
import com.rtuitlab.assemble.data.entities.FullAssembleDataOutDTO
import com.rtuitlab.assemble.data.entities.toAssembleIn
import com.rtuitlab.assemble.data.entities.toAssembleUpdateIn
import com.rtuitlab.assemble.domain.entities.Assemble
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AssembleApi(
    webClient: WebClient
) {
    val client = webClient.client

    suspend fun getAssembles(): List<AssembleForListOutDTO> {

        val response: HttpResponse = client.get("assemblies") {
        }

        val assemblies: List<AssembleForListOutDTO> = response.body()

        return assemblies
    }


    suspend fun getAssembleById(id: Long): FullAssembleDataOutDTO {
        val response = client.get("assemblies/$id") {}

        val assemble: FullAssembleDataOutDTO = response.body()

        return assemble
    }

    suspend fun getComponents(): List<ComponentOutDTO> {
        val response = client.get("components") { }
        val components: List<ComponentOutDTO> = response.body()

        return components
    }

    suspend fun createAssemble(assemble: Assemble): Long {
        val response = client.post("assemblies") {
            contentType(ContentType.Application.Json)
            setBody(assemble.toAssembleIn())
        }
        val new: AssembleOutDTO = response.body()
        return new.id
    }

    suspend fun updateAssemble(assemble: Assemble): Long {
        val response = client.patch("assemblies/${assemble.assembleId}") {
            contentType(ContentType.Application.Json)
            setBody(assemble.toAssembleUpdateIn())
        }
        return assemble.assembleId
    }

    suspend fun deleteAssemble(assembleId: Long) {
        val response = client.delete("assemblies/${assembleId}") {
        }
    }

    suspend fun generateSound(assembleId: Long) {
        val response = client.get("assemblies/${assembleId}/synthesize_speech") {
        }
    }


}