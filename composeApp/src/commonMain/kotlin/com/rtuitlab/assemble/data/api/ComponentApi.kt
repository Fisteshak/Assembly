package com.rtuitlab.assemble.data.api

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.WebClient
import com.rtuitlab.assemble.data.entities.ComponentOutDTO
import com.rtuitlab.assemble.data.safeGet

class ComponentApi(
    val webClient: WebClient
) {
    val client = webClient.client
    suspend fun getComponents(): RequestResult<List<ComponentOutDTO>> {
        return client.safeGet("components") { }
    }
}