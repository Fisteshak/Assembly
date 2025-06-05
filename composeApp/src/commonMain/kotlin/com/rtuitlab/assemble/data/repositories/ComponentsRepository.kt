package com.rtuitlab.assemble.data.repositories

import com.rtuitlab.assemble.data.RequestResult
import com.rtuitlab.assemble.data.api.ComponentApi
import com.rtuitlab.assemble.domain.entities.Component
import com.rtuitlab.assemble.domain.entities.toComponent

class ComponentsRepository(
    val api: ComponentApi
) {

    suspend fun getComponents(): RequestResult<List<Component>> {
        return api.getComponents().transform { list ->
            list.map { it.toComponent() }
        }
    }
}