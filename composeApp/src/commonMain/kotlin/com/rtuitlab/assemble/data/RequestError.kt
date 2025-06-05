package com.rtuitlab.assemble.data

sealed class RequestError {
    object NetworkError : RequestError()
    data class ApiError(val message: String?, val code: Int? = null) : RequestError()
    data class UnknownError(val message: String?) : RequestError()
}