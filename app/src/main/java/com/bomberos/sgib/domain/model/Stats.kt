package com.bomberos.sgib.domain.model

/**
 * Modelo de dominio para Estadísticas del Dashboard
 */
data class Stats(
    val totalActivos: Int,
    val totalInactivos: Int,
    val total: Int,
    val porRango: List<RangoCount>,
    val nuevosUltimoMes: Int
)

data class RangoCount(
    val rango: String,
    val cantidad: Int
)

