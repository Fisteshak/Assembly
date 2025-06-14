package com.rtuitlab.assemble.data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.logging.EMPTY
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import javax.print.PrintService
import javax.print.PrintServiceLookup

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(CIO) {
    config(this)

    install(Logging) {
        logger = Logger.EMPTY
        level = LogLevel.BODY
    }

    engine {
        // this: CIOEngineConfig
        maxConnectionsCount = 1000
        endpoint {
            // this: EndpointConfig
            maxConnectionsPerRoute = 100
            pipelineMaxSize = 20
            keepAliveTime = 5000
            connectTimeout = 5000
            connectAttempts = 5
        }

    }
}

fun discoverPrintServices() {
    val allPrintServices: Array<PrintService> = PrintServiceLookup.lookupPrintServices(null, null)

    for (printService in allPrintServices) {
        println("Print service name: " + printService.getName())
    }
}

