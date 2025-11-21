package com.bomberos.sgib.domain.model

// Modelo de dominio para Estadisticas del Dashboard
// Contiene metricas y datos agregados sobre los bomberos de la compania
// Se muestra en la pantalla principal (Dashboard) en forma de tarjetas informativas
// Permite visualizar rapidamente el estado de la compania

data class Stats(
    // Numero total de bomberos registrados en el sistema
    val totalBomberos: Int,
    
    // Numero total de bomberos con estado "Activo"
    // Bomberos que estan actualmente en servicio regular
    val totalActivos: Int,

    // Numero total de bomberos con estado "Inactivo"
    val totalInactivos: Int,
    
    // Numero total de bomberos con estado "Suspendido"
    val totalSuspendidos: Int,
    
    // Numero total de bomberos con estado "Dado de Baja"
    val totalBajas: Int,
    
    // Numero total de bomberos con estado "Renuncia"
    val totalRenuncias: Int,
    
    // Numero total de bomberos no activos (suma de suspendidos, bajas y renuncias)
    val totalNoActivos: Int,
    
    // Porcentaje de bomberos activos sobre el total
    val porcentajeActivos: Double
)
    // Porcentaje de bomberos activos sobre el total
    val porcentajeActivos: Double
)

// Este modelo se conecta con:
// - DashboardScreen: muestra todas las estadisticas en tarjetas
// - DashboardViewModel: calcula las estadisticas desde los datos de bomberos
// - BomberoRepository: proporciona los datos de bomberos para calcular estadisticas


