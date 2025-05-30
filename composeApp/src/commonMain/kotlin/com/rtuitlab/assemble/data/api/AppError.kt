package com.rtuitlab.assemble.data.api

sealed class AppError {
    object NetworkError : AppError()
    data class ApiError(val message: String?, val code: Int? = null) : AppError()
    data class UnknownError(val message: String?) : AppError()
}