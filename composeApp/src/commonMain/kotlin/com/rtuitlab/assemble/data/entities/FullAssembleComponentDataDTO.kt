package com.rtuitlab.assemble.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FullAssembleComponentDataDTO(
    @SerialName("component_id")
    val componentId: Long,
    val name: String,
    val amount: Long,
    @SerialName("link_to_photo")
    val linkToPhoto: String?,
    @SerialName("link_to_sound")
    val linkToSound: String?,
)