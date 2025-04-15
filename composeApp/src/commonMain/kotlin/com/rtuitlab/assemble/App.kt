package com.rtuitlab.assemble

import androidx.compose.runtime.*

import com.rtuitlab.assemble.ui.home.HomeScreen
import com.rtuitlab.assemble.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun App() {
    AppTheme(darkTheme = false) {
        HomeScreen()




    }
}

