package com.rtuitlab.assemble.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FullAssembleDataOutDTO(
    @SerialName("assemble_id")
    val assembleId: Long,
    val name: String,
    val instruction: String,
    @SerialName("amount_ready")
    val amountReady: Long,
    @SerialName("link_to_project")
    val linkToProject: String? = null,
    @SerialName("link_to_sound")
    val linkToSound: String? = null,
    @SerialName("user_id")
    val userId: Long,
    val components: List<FullAssembleComponentDataDTO>
)

