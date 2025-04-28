package com.rtuitlab.assemble.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComponentOutDTO(
    val id: Long,
    val name: String,
    @SerialName("link_to_image")
    val linkToImage: String? = null,
    val containers: List<String>? = null // TODO replace string with container type!
)