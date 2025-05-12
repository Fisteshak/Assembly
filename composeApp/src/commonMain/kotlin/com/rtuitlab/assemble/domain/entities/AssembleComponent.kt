package com.rtuitlab.assemble.domain.entities

import com.rtuitlab.assemble.data.entities.ComponentsAssembleInDTO
import com.rtuitlab.assemble.data.entities.FullAssembleComponentDataDTO

data class AssembleComponent(
    val componentId: Long,
    val name: String,
    val amount: Long,
    val linkToPhoto: String?,
    val linkToSound: String?,
    val isEdited: Boolean
)

fun FullAssembleComponentDataDTO.toAssembleComponent(): AssembleComponent {
    return AssembleComponent(
        componentId = this.componentId,
        name = this.name,
        amount = this.amount,
        linkToPhoto = this.linkToPhoto,
        linkToSound = this.linkToSound,
        isEdited = false
    )
}

fun Component.toAssembleComponent(): AssembleComponent {
    return AssembleComponent(
        componentId = this.id,
        name = this.name,
        amount = 1,
        linkToPhoto = null,
        linkToSound = null,
        isEdited = false
    )
}

fun ComponentsAssembleInDTO.toAssembleComponent(): AssembleComponent {
    return AssembleComponent(
        componentId = this.componentId,
        name = "",
        amount = this.amount,
        linkToPhoto = null,
        linkToSound = null,
        isEdited = false
    )
}
