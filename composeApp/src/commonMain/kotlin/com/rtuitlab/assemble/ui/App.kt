package com.rtuitlab.assemble.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rtuitlab.assemble.AssembleStore
import com.rtuitlab.assemble.di.koinModule
import com.rtuitlab.assemble.domain.entities.Assemble
import com.rtuitlab.assemble.domain.entities.AssembleComponent
import com.rtuitlab.assemble.ui.assemble.AssembleScreen
import com.rtuitlab.assemble.ui.home.HomeScreen
import com.rtuitlab.assemble.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
                        var assemble = Assemble(
                            -1,
                            "Новая сборка",
                            "",
                            0,
                            null,
                            null,
                            0,
                            mutableStateListOf()
                        )

                        store.accept(AssembleStore.Intent.UpdateCurrentAssemble(assemble))

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
                        HomeScreen(
                            onAssembleClick = { id ->
                                var assemble = assemblies.find { id == it.assembleId }
                                    ?.copy(components = assemblies.find { id == it.assembleId }?.components?.toMutableStateList())
                                store.accept(AssembleStore.Intent.UpdateCurrentAssemble(assemble))

                                navController.navigate(AssembleScreenRoute(id)) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    composable<AssembleScreenRoute> {

                        // it will probably be better to edit assemble inside AssembleScreen, it has access to store anyway
                        // it works fine now, although not convenient to use

                        val currentAssemble = uiState.currentAssemble
                        if (currentAssemble == null) return@composable

                        AssembleScreen(
                            currentAssemble,
                            onAmountChange = { index, amount ->
                                currentAssemble.components!![index] =
                                    currentAssemble.components!![index].copy(amount = amount)
                                store.accept(
                                    AssembleStore.Intent.UpdateCurrentAssemble(
                                        currentAssemble
                                    )
                                )

                            },
                            onNameChange = { index, name ->
                                currentAssemble.components!![index] =
                                    currentAssemble.components!![index].copy(
                                        name = name,
                                        componentId = -1
                                    )
                                store.accept(
                                    AssembleStore.Intent.UpdateCurrentAssemble(
                                        currentAssemble
                                    )
                                )

                            },
                            onMenuComponentClick = { index, component ->
                                currentAssemble.components!![index] = AssembleComponent(
                                    component.id,
                                    component.name,
                                    currentAssemble.components!![index].amount,
                                    null,
                                    null,
                                    false
                                )
                                store.accept(
                                    AssembleStore.Intent.UpdateCurrentAssemble(
                                        currentAssemble
                                    )
                                )

                            },
                            onAdd = {
                                currentAssemble.components!!.add(
                                    AssembleComponent(
                                        -1,
                                        "",
                                        1,
                                        null,
                                        null,
                                        true
                                    )
                                )
                                store.accept(
                                    AssembleStore.Intent.UpdateCurrentAssemble(
                                        currentAssemble
                                    )
                                )

                            },
                            onDeleteClick = {
                                currentAssemble.components!!.removeAt(it)
                                store.accept(
                                    AssembleStore.Intent.UpdateCurrentAssemble(
                                        currentAssemble
                                    )
                                )

                            },
                            onAssembleNameChange = {
                                store.accept(
                                    AssembleStore.Intent.UpdateCurrentAssemble(
                                        currentAssemble.copy(name = it)
                                    )
                                )
                            },
                            onNavigateBack = {
                                navController.navigate(HomeScreenRoute) {
                                    launchSingleTop = true
                                }
                            },
                            onInstructionChange = {
                                store.accept(
                                    AssembleStore.Intent.UpdateCurrentAssemble(
                                        currentAssemble.copy(instruction = it)
                                    )
                                )
                            }
                        )
                    }
                }

            }
        }
    }
}

