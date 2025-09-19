package com.rtuitlab.assemble.ui

import kotlinx.serialization.Serializable

@Serializable
object HomeScreenRoute

@Serializable
data class AssembleScreenRoute(
    val assembleId: Long? = null
)

@Serializable
data class SoundWindowRoute(
    val assembleId: Long? = null
)

@Serializable
data object ContainerScreenRoute

@Serializable
data object QrPrintScreenRoute

@Serializable
data object LoginScreenRoute