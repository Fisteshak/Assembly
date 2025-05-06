package com.rtuitlab.assemble.data.entities

import com.rtuitlab.assemble.domain.entities.Assemble
import kotlinx.serialization.Serializable

@Serializable
data class AssembleInDTO(
    val name: String,
    val instruction: String,
    val components: List<ComponentsAssembleInDTO>
)

fun Assemble.toAssembleIn(): AssembleInDTO {
    return AssembleInDTO(
        this.name, this.instruction.toString(),
        this.components?.map { it.toComponentsAssembleIn() } ?: emptyList()
    )
}


