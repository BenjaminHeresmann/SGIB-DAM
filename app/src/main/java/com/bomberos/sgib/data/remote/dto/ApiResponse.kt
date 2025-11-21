package com.bomberos.sgib.data.remote.dto

/**
 * Respuesta genérica de la API
 * Todas las respuestas del backend siguen este formato
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)

/**
 * Respuesta cuando no hay data específica
 */
data class MessageResponse(
    val success: Boolean,
    val message: String
)
