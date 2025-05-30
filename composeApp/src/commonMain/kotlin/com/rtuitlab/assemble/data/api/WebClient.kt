package com.rtuitlab.assemble.data.api

import io.ktor.client.HttpClient

class WebClient(
) {
    val client: HttpClient = httpClient()

    init {
        println("created WebClient")
    }
}