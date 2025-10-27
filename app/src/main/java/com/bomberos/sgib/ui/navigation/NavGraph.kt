package com.bomberos.sgib.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bomberos.sgib.ui.screens.dashboard.DashboardScreen
import com.bomberos.sgib.ui.screens.login.LoginScreen
import com.bomberos.sgib.ui.screens.bomberos.BomberosScreen
import com.bomberos.sgib.ui.screens.detalle.DetalleScreen
import com.bomberos.sgib.ui.screens.form.FormScreen
import com.bomberos.sgib.ui.screens.citaciones.CitacionesScreen
import com.bomberos.sgib.ui.screens.citaciones.detalle.CitacionDetalleScreen

/**
 * Grafo de navegación principal de la aplicación
 */
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    // Determinar ruta inicial basada en si el usuario está logueado
    val startDestination = Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize(),
        enterTransition = {
            fadeIn(animationSpec = tween(300)) +
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) +
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300)
                    )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) +
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) +
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )
        }
    ) {
        // Pantalla de Login
        composable(
            route = Screen.Login.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        // Limpiar el back stack para que no pueda volver al login
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Dashboard
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToBomberos = {
                    navController.navigate(Screen.BomberosList.route)
                },
                onNavigateToCitaciones = {
                    navController.navigate(Screen.CitacionesList.route)
                }
            )
        }

        // Pantalla de Lista de Bomberos
        composable(route = Screen.BomberosList.route) {
            BomberosScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetalle = { bomberoId ->
                    navController.navigate(Screen.BomberoDetail.createRoute(bomberoId))
                },
                onNavigateToCrear = {
                    navController.navigate(Screen.BomberoForm.route)
                }
            )
        }

        // Pantalla de Detalle de Bombero
        composable(
            route = Screen.BomberoDetail.route,
            arguments = listOf(
                navArgument("bomberoId") {
                    type = NavType.IntType
                }
            )
        ) {
            DetalleScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditar = { bomberoId ->
                    navController.navigate(Screen.BomberoForm.createRoute(bomberoId))
                }
            )
        }

        // Pantalla de Formulario de Bombero
        composable(
            route = Screen.BomberoForm.route,
            arguments = listOf(
                navArgument("bomberoId") {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                }
            )
        ) {
            FormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Pantalla de Lista de Citaciones
        composable(route = Screen.CitacionesList.route) {
            CitacionesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetalle = { citacionId ->
                    navController.navigate(Screen.CitacionDetail.createRoute(citacionId))
                }
            )
        }

        // Pantalla de Detalle de Citación
        composable(
            route = Screen.CitacionDetail.route,
            arguments = listOf(
                navArgument("citacionId") {
                    type = NavType.IntType
                }
            )
        ) {
            CitacionDetalleScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

