package com.rtuitlab.assemble.ui

@kotlinx.serialization.Serializable
object HomeScreenRoute

@kotlinx.serialization.Serializable
data class AssembleScreenRoute(
    val assembleId: Long? = null
)

@kotlinx.serialization.Serializable
data class SoundWindowRoute(
    val assembleId: Long? = null
)

@kotlinx.serialization.Serializable
data object ContainerScreenRoute

@kotlinx.serialization.Serializable
data object QrPrintScreenRoute

@kotlinx.serialization.Serializable
data object LoginScreenRoute