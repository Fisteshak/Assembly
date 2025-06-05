package com.rtuitlab.assemble.data

import com.rtuitlab.assemble.data.entities.TokenInfo
import com.rtuitlab.assemble.data.entities.UserInDTO
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


// TODO переместить в локальный файл?
private const val BASE_URL = "https://assemble.rtuitlab.dev/api/v1/"


var tokens: BearerTokens? = null
var user: UserInDTO? = null

expect fun httpClient(
    config: HttpClientConfig<*>.() -> Unit = {
        expectSuccess = true
        install(Auth) {
            bearer {
                loadTokens {
                    tokens
                }

                refreshTokens {

                    val user = UserInDTO("fisteshak", "fisteshak", 1, "fisteshak", "fisteshak")
                    val tokenInfo = login(client, user)
                    tokens = BearerTokens(
                        accessToken = tokenInfo.accessToken,
                        refreshToken = oldTokens?.refreshToken
                    )
                    tokens
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


suspend fun login(client: HttpClient, user: UserInDTO): TokenInfo {
    val response = client.post("users/login") {
        contentType(ContentType.Application.Json)
        setBody(user)
    }
    return response.body<TokenInfo>()
}