package com.rtuitlab.assemble.data.entities

import com.rtuitlab.assemble.domain.entities.Container
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContainerInDTO(
    val number: String,
    val room: String,
    val amount: Long,
    @SerialName("component_id")
    val componentId: Long,
)

fun Container.toContainerInDTO(): ContainerInDTO {
    return ContainerInDTO(
        number, room, amount, componentId
    )
}