package com.rtuitlab.assembly

import androidx.compose.runtime.*

import com.rtuitlab.assembly.ui.home.HomeScreen
import com.rtuitlab.assembly.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun App() {
    AppTheme(darkTheme = false) {
        HomeScreen()




    }
}

