package com.bomberos.sgib.domain.model

/**
 * Modelo de dominio para Usuario autenticado
 */
data class User(
    val id: Int,
    val email: String,
    val nombre: String,
    val rol: String,
    val tipo: String, // "admin" o "usuario"
    val activo: Boolean
)

