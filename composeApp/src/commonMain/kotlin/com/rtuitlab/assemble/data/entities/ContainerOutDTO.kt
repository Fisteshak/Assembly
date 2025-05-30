package com.rtuitlab.assemble.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContainerOutDTO(
    val number: String,
    val room: String,
    val amount: Long,
    @SerialName("component_id")
    val componentId: Long,
)