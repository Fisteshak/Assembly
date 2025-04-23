package com.rtuitlab.assemble.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class NetworkAssembleComponent(
    val componentId: Long,
    val name: String,
    val amount: Long,
    val linkToPhoto: String?,
    val linkToSound: String?,
)