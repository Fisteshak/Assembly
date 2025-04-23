package com.rtuitlab.assemble.data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// TODO переместить в локальный файл?
private const val BASE_URL = "https://assemble.rtuitlab.dev/api/v1/"

expect fun httpClient(
    config: HttpClientConfig<*>.() -> Unit = {

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXBlIjoiYWNjZXNzIiwic3ViIjo2LCJyb2xlIjoxLCJleHAiOjE3NDYwMTI0MjAsImlhdCI6MTc0NTQwNzYyMH0.Zb1jetjslZwBX6JAULdB09JjRxLlfmOyWh9asU1iW05788EuHljeuhAtwNhDXG4UboLkvSZU8U7O6ppjqTylU8ltI3AtQ2qD8FosvVP0QXAdnOU9P-RAySFn4ct-UFoUzYLvs5jOZaSL5SdBi_dxVuOMeoXtorCccc523_B-p4QdcWVrar7uBX8PiwyT1GRWZT52TtChd9iSuABK6KdNc-bLtPg8YVPa1zca7tupA_tjlsCuCnc7zLv4hz-acUqN3Je0hQvWb3KdofZNizNuJfyPMrAcbw-GCbD50mq2O_YONmlm15PZnwq_e1OEoWWPVC0u6TYveIXoWZ8qXka-nw",
                        refreshToken = null
                    )
                }
            }
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }

        defaultRequest {
            url(BASE_URL)
        }
    }
): HttpClient
