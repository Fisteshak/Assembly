package com.rtuitlab.assemble.data.entities

data class NetworkAssembleComponent(
    val componentId: Long,
    val name: String,
    val amount: Long,
    val linkToPhoto: String?,
    val linkToSound: String?,
)