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
) {
    companion object {
        fun new() = AssembleComponent(
            -1,
            "",
            1,
            null,
            null,
            true
        )
    }

    fun formatAmount(): String {
        val n = amount
        val end = n % 10
        val end2 = n % 100

        return "$n штук" +
                if (end2 in 11..19)
                    ""
                else
                    when (end) {
                        0L, 5L, 6L, 7L, 8L, 9L -> ""
                        1L -> "a"
                        2L, 3L, 4L -> "и"
                        else -> ""
                    }
    }

}

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
