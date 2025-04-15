package com.rtuitlab.assemble.data.entities

data class AssembleNetworkEntity(
    val assembleId: Long,
    val name: String,
    val instruction: String,
    val amountReady: Long,
    val linkToProject: String?,
    val linkToSound: String?,
    val userId: Long,
    val components: List<ComponentNetworkEntity>
)

