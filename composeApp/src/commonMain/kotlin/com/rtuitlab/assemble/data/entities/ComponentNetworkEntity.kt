package com.rtuitlab.assemble.data.entities

data class ComponentNetworkEntity(
    val componentId: Long,
    val name: String,
    val amount: Long,
    val linkToPhoto: String?,
    val linkToSound: String?,
)