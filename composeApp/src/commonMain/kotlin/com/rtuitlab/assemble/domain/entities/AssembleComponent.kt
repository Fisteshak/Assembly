package com.rtuitlab.assemble.domain.entities

import com.rtuitlab.assemble.data.entities.FullAssembleComponentDataDTO

data class AssembleComponent(
    val componentId: Long,
    val name: String,
    val amount: Long,
    val linkToPhoto: String?,
    val linkToSound: String?,
)

fun FullAssembleComponentDataDTO.toComponent(): AssembleComponent {
    return AssembleComponent(
        componentId = this.componentId,
        name = this.name,
        amount = this.amount,
        linkToPhoto = this.linkToPhoto,
        linkToSound = this.linkToSound
    )
}
