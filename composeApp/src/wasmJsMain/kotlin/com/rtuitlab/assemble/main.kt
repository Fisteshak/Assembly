package com.rtuitlab.assemble

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.compose.rememberNavController
import com.rtuitlab.assemble.ui.App
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalBrowserHistoryApi
fun main() {

    ComposeViewport(document.body!!) {
        val navController = rememberNavController()
        App(navController)


    }
}