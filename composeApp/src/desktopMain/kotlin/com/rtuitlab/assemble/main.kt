package com.rtuitlab.assemble

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.rememberNavController
import com.rtuitlab.assemble.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Assemble",
    ) {
        val navController = rememberNavController()
        App(navController)
    }
}