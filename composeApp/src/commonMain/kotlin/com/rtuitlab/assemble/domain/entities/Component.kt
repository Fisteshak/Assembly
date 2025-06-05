package com.rtuitlab.assemble.domain.entities

import com.rtuitlab.assemble.data.entities.ComponentOutDTO

data class Component(
    val id: Long,
    val name: String,
    val linkToImage: String? = null,
    val containers: List<String>? // TODO replace string with container type!
) {
    companion object {
        fun createEmpty(): Component = Component(-1, "", null, null)
    }
}

fun ComponentOutDTO.toComponent(): Component {
    return Component(
        id = this.id,
        name = this.name,
        linkToImage = this.linkToImage,
        containers = this.containers
    )
}



