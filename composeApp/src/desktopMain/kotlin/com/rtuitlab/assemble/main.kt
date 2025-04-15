package com.rtuitlab.assemble

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.rtuitlab.assemble.ui.home.HomeScreen
import com.rtuitlab.assemble.ui.theme.AppTheme

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Assemble",
    ) {
        AppTheme {
            HomeScreen()

        }
    }
}