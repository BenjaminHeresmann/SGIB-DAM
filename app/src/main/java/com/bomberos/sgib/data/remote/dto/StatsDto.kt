package com.bomberos.sgib.data.remote.dto

/**
 * DTO para estadísticas
 */
data class StatsResponse(
    val success: Boolean,
    val data: StatsDto
)

data class StatsDto(
    val totalActivos: Int,
    val totalInactivos: Int,
    val total: Int,
    val porRango: List<RangoCountDto>,
    val nuevosUltimoMes: Int
)

data class RangoCountDto(
    val rango: String,
    val cantidad: Int
)

