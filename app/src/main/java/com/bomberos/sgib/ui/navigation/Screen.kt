package com.bomberos.sgib.ui.navigation

/**
 * Rutas de navegación de la aplicación
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object BomberosList : Screen("bomberos")
    object BomberoDetail : Screen("bomberos/{bomberoId}") {
        fun createRoute(bomberoId: Int) = "bomberos/$bomberoId"
    }
    object BomberoForm : Screen("bomberos/form?bomberoId={bomberoId}") {
        fun createRoute(bomberoId: Int? = null) =
            if (bomberoId != null) "bomberos/form?bomberoId=$bomberoId"
            else "bomberos/form"
    }
    object CitacionesList : Screen("citaciones")
    object CitacionDetail : Screen("citaciones/{citacionId}") {
        fun createRoute(citacionId: Int) = "citaciones/$citacionId"
    }
}

