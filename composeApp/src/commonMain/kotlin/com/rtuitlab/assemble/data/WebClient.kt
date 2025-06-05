package com.rtuitlab.assemble.data

import io.ktor.client.HttpClient

class WebClient(
) {
    val client: HttpClient = httpClient()

    init {
        println("created WebClient")
    }
}