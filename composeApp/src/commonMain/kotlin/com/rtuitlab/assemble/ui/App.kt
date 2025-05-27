package com.rtuitlab.assemble.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.AssembleStore
import com.rtuitlab.assemble.di.koinModule
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.ui.assemble.AssembleScreen
import com.rtuitlab.assemble.ui.home.HomeScreen
import com.rtuitlab.assemble.ui.sound.SoundWindow
import com.rtuitlab.assemble.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.koin.compose.getKoin
import org.koin.core.context.startKoin

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

    startKoin {

        modules(koinModule)
    }





    AppTheme(darkTheme = false) {
        val store: AssembleStore = getKoin().get()
        val uiState by store.stateFlow.collectAsStateWithLifecycle()
        val assemblies = uiState.assemblies
        Surface(modifier = Modifier.fillMaxSize()) {
            val backStack = navController.currentBackStack
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val shouldShowBackButton =
                navBackStackEntry?.destination?.hasRoute(HomeScreenRoute::class) == false
            printBackStack(backStack.value)

            Row(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                NavigationBar(
                    onAssembleClick = {
                        val assemble = Assemble(
                            -1,
                            "Новая сборка",
                            "",
                            0,
                            null,
                            null,
                            0,
                            mutableStateListOf()
                        )

                        store.accept(AssembleStore.Intent.SetCurrentAssemble(assemble))

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

                NavHost(
                    navController, startDestination = HomeScreenRoute,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None },
                ) {
                    composable<HomeScreenRoute> {

                        LaunchedEffect(Unit) {
                            while (true) {
                                store.accept(AssembleStore.Intent.FetchAssemblies)
                                delay(5000)
                            }
                        }

                        HomeScreen(
                            onAssembleClick = { id ->

                                navController.navigate(AssembleScreenRoute(id)) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    composable<AssembleScreenRoute> {

                        AssembleScreen(
                            onNavigateBack = {
                                navController.navigate(HomeScreenRoute) {
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToSoundWindow = {
                                navController.navigate(SoundWindowRoute(null)) {
                                    launchSingleTop = true

                                }
                            }
                        )
                    }
                    // this dialog shouldn't be separate destination, but anyway it works
                    dialog<SoundWindowRoute> {
                        SoundWindow(
                            onNavigateBack = { navController.popBackStack() },
                            modifier = Modifier
                        )
                    }
                }

            }
        }
    }
}

