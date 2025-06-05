package com.rtuitlab.assemble

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import com.rtuitlab.assemble.ui.App
import java.awt.Dimension

fun main() = application {
    val state = rememberWindowState(placement = WindowPlacement.Maximized)
    Window(
        onCloseRequest = ::exitApplication,
        title = "Assemble",
        state = state,
    ) {
        val navController = rememberNavController()

        window.minimumSize = Dimension(800, 600)
        App(navController)

    }
}