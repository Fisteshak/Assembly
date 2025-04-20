package com.rtuitlab.assemble.ui

import kotlinx.serialization.Serializable

@Serializable
object HomeScreenRoute

@Serializable
data class AssembleScreenRoute(
    val assembleId: Long? = null
)