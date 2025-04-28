package com.rtuitlab.assemble.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class AssembleForListOutDTO(
    val id: Long,
    val name: String,
)

