package com.rtuitlab.assemble.data.entities

import kotlinx.serialization.Serializable


@Serializable
data class UserInDTO(
    val name: String,
    val lastname: String,
    val role: Long,
    val email: String,
    val phone: String
)