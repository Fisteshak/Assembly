package com.rtuitlab.assemble.data.entities

data class NetworkAssemble(
    val assembleId: Long,
    val name: String,
    val instruction: String,
    val amountReady: Long,
    val linkToProject: String?,
    val linkToSound: String?,
    val userId: Long,
    val components: List<NetworkAssembleComponent>
)

