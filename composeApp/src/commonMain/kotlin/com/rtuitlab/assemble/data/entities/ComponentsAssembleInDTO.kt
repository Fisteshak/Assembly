package com.rtuitlab.assemble.data.entities

import com.rtuitlab.assemble.domain.entities.AssembleComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComponentsAssembleInDTO(
    @SerialName("component_id")
    val componentId: Long,
    val amount: Long,
)

fun AssembleComponent.toComponentsAssembleIn(): ComponentsAssembleInDTO {
    return ComponentsAssembleInDTO(this.componentId, this.amount)
}

