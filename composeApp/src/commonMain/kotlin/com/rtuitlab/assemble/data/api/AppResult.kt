package com.rtuitlab.assemble.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Failure(val error: AppError) : AppResult<Nothing>()

    fun <R> transform(block: (T) -> R): AppResult<R> = when (this) {
        is Success -> Success(block(this.data))
        is Failure -> Failure(this.error)
    }

}

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): AppResult<T> {
    return try {
        val response = request { block() }
        AppResult.Success(response.body())
    } catch (e: ClientRequestException) {
        AppResult.Failure(AppError.ApiError(e.message, e.response.status.value))
    } catch (e: ServerResponseException) {
        AppResult.Failure(AppError.ApiError(e.message, e.response.status.value))
    } catch (_: IOException) {
        AppResult.Failure(AppError.NetworkError)
    } catch (_: SerializationException) {
        AppResult.Failure(AppError.NetworkError)
    } catch (_: UnresolvedAddressException) {
        AppResult.Failure(AppError.NetworkError)
    } catch (e: Exception) {
        AppResult.Failure(AppError.UnknownError(e.message))
    }
}

suspend inline fun <reified T> HttpClient.safeGet(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): AppResult<T> = safeRequest {
    url(urlString)
    method = HttpMethod.Companion.Get
    block()
}

suspend inline fun <reified T> HttpClient.safePost(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): AppResult<T> = safeRequest {
    url(urlString)
    method = HttpMethod.Companion.Post
    block()
}

suspend inline fun <reified T> HttpClient.safePatch(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): AppResult<T> = safeRequest {
    url(urlString)
    method = HttpMethod.Companion.Patch
    block()
}

suspend inline fun <reified T> HttpClient.safeDelete(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): AppResult<T> = safeRequest {
    url(urlString)
    method = HttpMethod.Companion.Delete
    block()
}