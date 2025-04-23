package com.rtuitlab.assemble.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class NetworkAssemble(
    val id: Long,
    val name: String,
    val instruction: String? = null,
    val amountReady: Long? = null,
    val linkToProject: String? = null,
    val linkToSound: String? = null,
    val userId: Long? = null,
    val components: List<NetworkAssembleComponent>? = null
)

