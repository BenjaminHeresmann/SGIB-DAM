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

// Funcion composable NavGraph: Define el grafo de navegacion principal de la aplicacion
// El grafo de navegacion es la estructura que conecta todas las pantallas entre si
// Determina como el usuario se mueve de una pantalla a otra
// Es similar a un mapa que muestra todas las rutas posibles en la aplicacion

// Anotacion Composable: Indica que esta funcion es parte de la UI de Jetpack Compose
@Composable
fun NavGraph(
    // NavHostController: Controlador que gestiona la navegacion
    // Proporciona metodos como navigate(), popBackStack(), navigateUp()
    // rememberNavController(): crea y recuerda el NavController durante recomposiciones
    navController: NavHostController = rememberNavController()
) {
    // Determinar ruta inicial: la primera pantalla que se muestra al abrir la app
    // En este caso siempre es la pantalla de Login
    // En una implementacion mas avanzada, se verificaria si hay una sesion activa
    // y se mostraria Dashboard si el usuario ya esta logueado
    val startDestination = Screen.Login.route

    // NavHost: Contenedor que aloja todas las pantallas navegables
    // Es el componente principal del sistema de navegacion de Compose
    NavHost(
        // Controlador que gestiona la navegacion
        navController = navController,

        // Ruta de inicio: primera pantalla que se muestra
        startDestination = startDestination,

        // Modifier para ocupar todo el espacio disponible
        modifier = Modifier.fillMaxSize(),

        // Animaciones de entrada: se ejecutan cuando una nueva pantalla aparece
        // fadeIn: la pantalla nueva aparece gradualmente (de transparente a opaca)
        // slideInHorizontally: la pantalla nueva se desliza desde la derecha
        // tween(300): duracion de la animacion en milisegundos
        enterTransition = {
            fadeIn(animationSpec = tween(300)) +
                    slideInHorizontally(
                        initialOffsetX = { it },  // it es el ancho de la pantalla
                        animationSpec = tween(300)
                    )
        },

        // Animaciones de salida: se ejecutan cuando la pantalla actual desaparece
        // fadeOut: la pantalla actual desaparece gradualmente (de opaca a transparente)
        // slideOutHorizontally: la pantalla actual se desliza hacia la izquierda
        exitTransition = {
            fadeOut(animationSpec = tween(300)) +
                    slideOutHorizontally(
                        targetOffsetX = { -it },  // -it mueve hacia la izquierda
                        animationSpec = tween(300)
                    )
        },

        // Animaciones de entrada al retroceder: cuando se usa el boton atras
        // La pantalla anterior vuelve a aparecer desde la izquierda
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) +
                    slideInHorizontally(
                        initialOffsetX = { -it },  // viene desde la izquierda
                        animationSpec = tween(300)
                    )
        },

        // Animaciones de salida al retroceder: cuando se usa el boton atras
        // La pantalla actual se desliza hacia la derecha y desaparece
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) +
                    slideOutHorizontally(
                        targetOffsetX = { it },  // se mueve hacia la derecha
                        animationSpec = tween(300)
                    )
        }
    ) {
        // DEFINICION DE RUTAS: Cada composable() define una pantalla navegable

        // Pantalla de Login: Primera pantalla de la aplicacion
        // Ruta: "login" (definida en Screen.Login.route)
        composable(
            route = Screen.Login.route,
            // Animaciones personalizadas para esta pantalla (solo fade, sin slide)
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            // LoginScreen: Componente composable de la pantalla de login
            // Conecta con LoginScreen.kt y LoginViewModel
            LoginScreen(
                // Callback onLoginSuccess: se ejecuta cuando el login es exitoso
                // Parametro: ninguno (Unit), solo indica que el login fue exitoso
                onLoginSuccess = {
                    // navigate: navega a la pantalla de Dashboard
                    navController.navigate(Screen.Dashboard.route) {
                        // Limpiar el back stack: elimina Login del historial de navegacion
                        // popUpTo: elimina todas las pantallas hasta Login (inclusive)
                        // Esto evita que el usuario pueda volver al Login con el boton atras
                        // Es importante para pantallas de autenticacion
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Dashboard: Pantalla principal despues del login
        // Ruta: "dashboard" (definida en Screen.Dashboard.route)
        // Muestra estadisticas y accesos rapidos a modulos
        composable(route = Screen.Dashboard.route) {
            // DashboardScreen: Componente composable del dashboard
            // Conecta con DashboardScreen.kt y DashboardViewModel
            DashboardScreen(
                // Callback onNavigateToBomberos: navega a la lista de bomberos
                onNavigateToBomberos = {
                    navController.navigate(Screen.BomberosList.route)
                },
                // Callback onNavigateToCitaciones: navega a la lista de citaciones
                onNavigateToCitaciones = {
                    navController.navigate(Screen.CitacionesList.route)
                }
            )
        }

        // Pantalla de Lista de Bomberos: Muestra todos los bomberos registrados
        // Ruta: "bomberos" (definida en Screen.BomberosList.route)
        composable(route = Screen.BomberosList.route) {
            // BomberosScreen: Componente composable de la lista de bomberos
            // Conecta con BomberosScreen.kt y BomberosViewModel
            BomberosScreen(
                // Callback onNavigateBack: vuelve a la pantalla anterior (Dashboard)
                // popBackStack: elimina la pantalla actual del stack y vuelve a la anterior
                onNavigateBack = { navController.popBackStack() },

                // Callback onNavigateToDetalle: navega al detalle de un bombero
                // Parametro bomberoId: ID del bombero seleccionado
                onNavigateToDetalle = { bomberoId ->
                    // createRoute: crea la ruta con el ID del bombero
                    // Ejemplo: "bomberos/5" para mostrar el bombero con ID 5
                    navController.navigate(Screen.BomberoDetail.createRoute(bomberoId))
                },

                // Callback onNavigateToCrear: navega al formulario para crear un bombero nuevo
                onNavigateToCrear = {
                    // Sin parametros significa crear un bombero nuevo
                    navController.navigate(Screen.BomberoForm.route)
                }
            )
        }

        // Pantalla de Detalle de Bombero: Muestra informacion completa de un bombero
        // Ruta: "bomberos/{bomberoId}" (definida en Screen.BomberoDetail.route)
        // {bomberoId} es un parametro de ruta que se extrae automaticamente
        composable(
            route = Screen.BomberoDetail.route,
            // arguments: Lista de argumentos que esta ruta espera recibir
            arguments = listOf(
                // navArgument: Define un argumento de navegacion
                navArgument("bomberoId") {
                    // type: Tipo del argumento (Int en este caso)
                    // NavType.IntType convierte el String de la URL a Int automaticamente
                    type = NavType.IntType
                }
            )
        ) {
            // DetalleScreen: Componente composable del detalle de bombero
            // Conecta con DetalleScreen.kt y DetalleViewModel
            // El ViewModel extrae bomberoId del SavedStateHandle automaticamente
            DetalleScreen(
                // Callback onNavigateBack: vuelve a la lista de bomberos
                onNavigateBack = { navController.popBackStack() },

                // Callback onNavigateToEditar: navega al formulario de edicion
                // Parametro bomberoId: ID del bombero a editar
                onNavigateToEditar = { bomberoId ->
                    // createRoute con ID: abre el formulario en modo edicion
                    navController.navigate(Screen.BomberoForm.createRoute(bomberoId))
                }
            )
        }

        // Pantalla de Formulario de Bombero: Crear o editar un bombero
        // Ruta: "bomberos/form?bomberoId={bomberoId}" (definida en Screen.BomberoForm.route)
        // bomberoId es un query parameter opcional (puede ser -1 para crear nuevo)
        composable(
            route = Screen.BomberoForm.route,
            arguments = listOf(
                navArgument("bomberoId") {
                    // type: Tipo Int para el ID del bombero
                    type = NavType.IntType
                    // defaultValue: Valor por defecto si no se proporciona
                    // -1 indica que se va a crear un bombero nuevo
                    defaultValue = -1
                    // nullable: false porque siempre tiene un valor (minimo -1)
                    nullable = false
                }
            )
        ) {
            // FormScreen: Componente composable del formulario
            // Conecta con FormScreen.kt y FormViewModel
            // El ViewModel usa bomberoId para determinar si crea o edita
            FormScreen(
                // Callback onNavigateBack: vuelve a la pantalla anterior
                // Puede ser BomberosScreen o DetalleScreen dependiendo de donde vino
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Pantalla de Lista de Citaciones: Muestra todas las citaciones
        // Ruta: "citaciones" (definida en Screen.CitacionesList.route)
        composable(route = Screen.CitacionesList.route) {
            // CitacionesScreen: Componente composable de la lista de citaciones
            // Conecta con CitacionesScreen.kt y CitacionesViewModel
            CitacionesScreen(
                // Callback onNavigateBack: vuelve al Dashboard
                onNavigateBack = { navController.popBackStack() },

                // Callback onNavigateToDetalle: navega al detalle de una citacion
                // Parametro citacionId: ID de la citacion seleccionada
                onNavigateToDetalle = { citacionId ->
                    // createRoute: crea la ruta con el ID de la citacion
                    navController.navigate(Screen.CitacionDetail.createRoute(citacionId))
                }
            )
        }

        // Pantalla de Detalle de Citacion: Muestra informacion completa de una citacion
        // Ruta: "citaciones/{citacionId}" (definida en Screen.CitacionDetail.route)
        composable(
            route = Screen.CitacionDetail.route,
            arguments = listOf(
                // navArgument para el ID de la citacion
                navArgument("citacionId") {
                    // type: Int para el ID de la citacion
                    type = NavType.IntType
                }
            )
        ) {
            // CitacionDetalleScreen: Componente composable del detalle de citacion
            // Conecta con CitacionDetalleScreen.kt y CitacionDetalleViewModel
            // El ViewModel extrae citacionId automaticamente
            CitacionDetalleScreen(
                // Callback onNavigateBack: vuelve a la lista de citaciones
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
// Este NavGraph es el corazon de la navegacion de la aplicacion
// Conecta todas las pantallas y define como el usuario se mueve entre ellas
// Cada Screen tiene callbacks que permiten navegar a otras pantallas
// Los ViewModels no conocen la navegacion, solo las Screens usan el navController
// Esto mantiene una separacion clara entre logica de negocio y navegacion


