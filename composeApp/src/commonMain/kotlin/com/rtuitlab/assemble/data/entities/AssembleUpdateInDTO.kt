package com.rtuitlab.assemble.data.entities

import com.rtuitlab.assemble.domain.entities.Assemble
import kotlinx.serialization.Serializable

@Serializable
data class AssembleUpdateInDTO(
    val name: String,
    val instruction: String,
    val components: List<ComponentsAssembleInDTO>
)

fun Assemble.toAssembleUpdateIn(): AssembleUpdateInDTO {
    return AssembleUpdateInDTO(
        this.name, this.instruction.toString(),
        this.components?.map { it.toComponentsAssembleIn() } ?: emptyList()
    )
}


