package com.rtuitlab.assemble.domain.entities

import com.rtuitlab.assemble.data.entities.AssembleForListOutDTO
import com.rtuitlab.assemble.data.entities.FullAssembleDataOutDTO

data class Assemble(
    val assembleId: Long,
    val name: String,
    val instruction: String?,
    val amountReady: Long?,
    val linkToProject: String?,
    val linkToSound: String?,
    val userId: Long?,
    val components: List<AssembleComponent>?
)

fun AssembleForListOutDTO.toAssemble(): Assemble {

    return Assemble(
        assembleId = this.id,
        name = this.name,
        instruction = null,
        amountReady = null,
        linkToProject = null,
        linkToSound = null,
        userId = null,
        components = null,
    )
}

fun FullAssembleDataOutDTO.toAssemble(): Assemble {

    val mappedComponents = this.components.map { networkComponent ->
        networkComponent.toComponent()
    }

    return Assemble(
        assembleId = this.assembleId,
        name = this.name,
        instruction = this.instruction,
        amountReady = this.amountReady,
        linkToProject = this.linkToProject,
        linkToSound = this.linkToSound,
        userId = this.userId,
        components = mappedComponents
    )
}