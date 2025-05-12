package com.rtuitlab.assemble.data.entities

import com.rtuitlab.assemble.domain.entities.Assemble
import kotlinx.serialization.Serializable

@Serializable
data class AssembleOutDTO(
    val id: Long,
    val name: String,
    val instruction: String,
    val components: List<ComponentsAssembleInDTO>
)

fun Assemble.toAssembleOut(): AssembleOutDTO {
    return AssembleOutDTO(
        this.assembleId,
        this.name, this.instruction.toString(),
        this.components?.map { it.toComponentsAssembleIn() } ?: emptyList()
    )
}


