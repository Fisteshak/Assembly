package com.rtuitlab.assemble.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.ui.assemble.AssembleScreen
import com.rtuitlab.assemble.ui.assemble.store.AssembleStore
import com.rtuitlab.assemble.ui.container.ContainerScreen
import com.rtuitlab.assemble.ui.container.store.ContainerStore
import com.rtuitlab.assemble.ui.home.HomeScreen
import com.rtuitlab.assemble.ui.qrprint.QrPrintScreen
import com.rtuitlab.assemble.ui.sound.SoundWindow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.compose.getKoin

fun printBackStack(backStack: List<NavBackStackEntry>) {
    print("backStack:")
    for (x in backStack) {
        print(" -> " + x.destination.route + (x.arguments?.toString()?.let { " $it" } ?: ""))


    }
    println()
}


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NavHost(navController: NavHostController) {

    val assembleStore: AssembleStore = getKoin().get()
    val assembleUiState by assembleStore.stateFlow.collectAsStateWithLifecycle()
    val containerStore: ContainerStore = getKoin().get()
    val containerUiState by containerStore.stateFlow.collectAsStateWithLifecycle()
    Surface(modifier = Modifier.fillMaxSize()) {
        val backStack by navController.currentBackStack.collectAsStateWithLifecycle()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val shouldShowBackButton =

            navBackStackEntry?.destination?.hasRoute(HomeScreenRoute::class) == false


        val shouldShowNavBar =
            backStack.any { it.destination.hasRoute(HomeScreenRoute::class) }
//            navBackStackEntry?.destination?.hasRoute(HomeScreenRoute::class) == true
        printBackStack(backStack)

        Row(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            if (shouldShowNavBar) {

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

                        assembleStore.accept(AssembleStore.Intent.SetCurrentAssemble(assemble))

                        navController.navigate(AssembleScreenRoute(null)) {
                            launchSingleTop = true
                            popUpTo(HomeScreenRoute)
                        }

                    },
                    onContainerClick = {

                        containerStore.accept(
                            ContainerStore.Intent.SetNewCurrentContainer
                        )

                        navController.navigate(ContainerScreenRoute) {
                            launchSingleTop = true
                            popUpTo(HomeScreenRoute)

                        }
                    },
                    onQrCodeClick = {
                        navController.navigate(QrPrintScreenRoute) {
                            launchSingleTop = true
                            popUpTo(HomeScreenRoute)
                        }
                    },
                    onBackClick = if (shouldShowBackButton) {
                        {
                            navController.popBackStack()
                        }
                    } else null,
                    modifier = Modifier
                )
            }

            NavHost(
                navController, startDestination = HomeScreenRoute,
                enterTransition = { fadeIn(tween(150)) },
                exitTransition = { fadeOut(tween(150)) },
                popEnterTransition = { fadeIn(tween(150)) },
                popExitTransition = { fadeOut(tween(150)) },
            ) {
                composable<HomeScreenRoute> {

                    LaunchedEffect(Unit) {
                        assembleStore.accept(AssembleStore.Intent.FetchAssemblies)
                        containerStore.accept(ContainerStore.Intent.GetContainers())
                    }

                    HomeScreen(
                        onAssembleClick = { id ->
                            navController.navigate(AssembleScreenRoute(id)) {
                                launchSingleTop = true
                                popUpTo(HomeScreenRoute)
                            }
                        },
                        onContainerClick = { number ->
                            navController.navigate(ContainerScreenRoute) {
                                launchSingleTop = true
                                popUpTo(HomeScreenRoute)
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

                composable<ContainerScreenRoute> {
                    ContainerScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToPrintScreen = {
                            navController.navigate(QrPrintScreenRoute) {
                                launchSingleTop = true
                                popUpTo(HomeScreenRoute)
                            }
                        },
                        modifier = Modifier
                    )
                }

                composable<QrPrintScreenRoute> {
                    QrPrintScreen(
                        onNavigateBack = { navController.popBackStack() },
                        modifier = Modifier
                    )
                }

//                composable<LoginScreenRoute> {}
            }

        }
    }

}

