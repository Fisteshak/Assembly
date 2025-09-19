package com.rtuitlab.assemble.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.rtuitlab.assemble.di.koinModule
import com.rtuitlab.assemble.ui.theme.AppTheme
import org.koin.compose.KoinApplication

@Composable
fun App(
    navController: NavHostController,
) {
    KoinApplication(application = {
        modules(koinModule)
    }) {
        AppTheme(darkTheme = false) {
            NavHost(navController)
        }
    }
}