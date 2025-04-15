package com.rtuitlab.assemble.domain.entities

import com.rtuitlab.assemble.data.entities.ComponentNetworkEntity

data class Component(
    val componentId: Long,
    val name: String,
    val amount: Long,
    val linkToPhoto: String?,
    val linkToSound: String?,
)

fun ComponentNetworkEntity.toComponent(): Component {
    return Component(
        componentId = this.componentId,
        name = this.name,
        amount = this.amount,
        linkToPhoto = this.linkToPhoto,
        linkToSound = this.linkToSound
    )
}
