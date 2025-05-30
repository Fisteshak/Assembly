package com.rtuitlab.assemble.domain.entities

import com.rtuitlab.assemble.data.entities.ContainerOutDTO

data class Container(
    val number: String,
    val room: String,
    val amount: Long,
    val componentId: Long,
)

fun ContainerOutDTO.toContainer(): Container {
    return Container(
        number, room, amount, componentId
    )
}