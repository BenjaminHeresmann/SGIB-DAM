package com.bomberos.sgib.ui.navigation

// Clase sellada Screen: Define todas las rutas de navegacion de la aplicacion
// Una sealed class es una clase que restringe la jerarquia de herencia
// Solo puede tener subclases definidas en el mismo archivo
// Se usa para representar un conjunto fijo de pantallas en la app

// Cada objeto dentro de Screen representa una pantalla de la aplicacion
// El parametro route es la URL interna usada por el sistema de navegacion de Compose

sealed class Screen(val route: String) {

    // Pantalla Login: Pantalla de inicio de sesion
    // Ruta: "login"
    // Es la primera pantalla que ve el usuario si no esta autenticado
    // Conecta con LoginScreen.kt
    object Login : Screen("login")

    // Pantalla Dashboard: Pantalla principal despues del login
    // Ruta: "dashboard"
    // Muestra estadisticas de bomberos y accesos rapidos a modulos
    // Conecta con DashboardScreen.kt
    object Dashboard : Screen("dashboard")

    // Pantalla BomberosList: Lista de todos los bomberos
    // Ruta: "bomberos"
    // Muestra una lista completa de bomberos con busqueda y filtros
    // Conecta con BomberosScreen.kt
    object BomberosList : Screen("bomberos")

    // Pantalla BomberoDetail: Detalle de un bombero especifico
    // Ruta: "bomberos/{bomberoId}"
    // {bomberoId} es un parametro de ruta que se reemplaza con el ID real
    // Ejemplo de ruta real: "bomberos/1", "bomberos/15"
    // Conecta con DetalleScreen.kt
    object BomberoDetail : Screen("bomberos/{bomberoId}") {
        // Funcion helper para crear la ruta con el ID especifico
        // Parametro bomberoId: ID del bombero a mostrar
        // Retorna: String con la ruta completa
        // Ejemplo: createRoute(5) retorna "bomberos/5"
        fun createRoute(bomberoId: Int) = "bomberos/$bomberoId"
    }

    // Pantalla BomberoForm: Formulario para crear o editar un bombero
    // Ruta: "bomberos/form?bomberoId={bomberoId}"
    // El parametro bomberoId es opcional (query parameter con ?)
    // Si bomberoId existe, se edita ese bombero
    // Si bomberoId es null, se crea un bombero nuevo
    // Ejemplos: "bomberos/form" (crear), "bomberos/form?bomberoId=5" (editar)
    // Conecta con FormScreen.kt
    object BomberoForm : Screen("bomberos/form?bomberoId={bomberoId}") {
        // Funcion helper para crear la ruta con o sin ID
        // Parametro bomberoId: ID del bombero a editar, o null para crear nuevo
        // Retorna: String con la ruta completa
        // Ejemplo: createRoute(5) retorna "bomberos/form?bomberoId=5"
        // Ejemplo: createRoute(null) retorna "bomberos/form"
        fun createRoute(bomberoId: Int? = null) =
            if (bomberoId != null) "bomberos/form?bomberoId=$bomberoId"
            else "bomberos/form"
    }

    // Pantalla CitacionesList: Lista de todas las citaciones
    // Ruta: "citaciones"
    // Muestra lista de citaciones con filtros por estado y tipo
    // Conecta con CitacionesScreen.kt
    object CitacionesList : Screen("citaciones")

    // Pantalla CitacionDetail: Detalle de una citacion especifica
    // Ruta: "citaciones/{citacionId}"
    // {citacionId} es un parametro de ruta que se reemplaza con el ID real
    // Ejemplo de ruta real: "citaciones/1", "citaciones/10"
    // Conecta con CitacionDetalleScreen.kt
    object CitacionDetail : Screen("citaciones/{citacionId}") {
        // Funcion helper para crear la ruta con el ID especifico
        // Parametro citacionId: ID de la citacion a mostrar
        // Retorna: String con la ruta completa
        // Ejemplo: createRoute(3) retorna "citaciones/3"
        fun createRoute(citacionId: Int) = "citaciones/$citacionId"
    }
}
// Este archivo se conecta con:
// - NavGraph.kt: usa estas rutas para definir el grafo de navegacion
// - Todas las Screens: usan navController.navigate(Screen.X.route) para navegar
// - ViewModels: usan estas rutas para navegar despues de operaciones exitosas

// Patron de navegacion:
// 1. Usuario toca un boton en una Screen
// 2. Screen llama a navController.navigate(Screen.Destino.route)
// 3. NavGraph intercepta la ruta y muestra la Screen correspondiente
// 4. Si la ruta tiene parametros, NavGraph los extrae y pasa a la Screen


