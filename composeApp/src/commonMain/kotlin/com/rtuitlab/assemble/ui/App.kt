package com.rtuitlab.assemble.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.rtuitlab.assemble.ui.assemble.AssembleScreen
import com.rtuitlab.assemble.ui.home.HomeScreen
import com.rtuitlab.assemble.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

fun printBackStack(backStack: List<NavBackStackEntry>) {
    print("backStack:")
    for (x in backStack) {
        print(" -> " + x.destination.route + (x.arguments?.toString()?.let { " $it" } ?: ""))


    }
    println()
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun App(navController: NavHostController) {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val backStack = navController.currentBackStack
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val shouldShowBackButton =
                navBackStackEntry?.destination?.hasRoute(HomeScreenRoute::class) == false
            printBackStack(backStack.value)

            Row(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                NavigationBar(
                    onAssembleClick = {
                        navController.navigate(AssembleScreenRoute(null)) {
                            launchSingleTop = true
                        }

                    },
                    onContainerClick = {},
                    onQrCodeClick = {},
                    onBackClick = if (shouldShowBackButton) {
                        {
                            navController.popBackStack(HomeScreenRoute, false)
                        }
                    } else null,
                    modifier = Modifier
                )

                NavHost(navController, startDestination = HomeScreenRoute) {
                    composable<HomeScreenRoute> {
                        HomeScreen(
                            onAssembleClick = {
                                println("clicked"); navController.navigate(AssembleScreenRoute(it)) {
                                launchSingleTop = true

                            }
                            }
                        )
                    }
                    composable<AssembleScreenRoute> {
                        val assembleId =
                            navBackStackEntry?.toRoute<AssembleScreenRoute>()?.assembleId

                        AssembleScreen(assembleId)
                    }
                }

            }
        }
    }
}

