package com.bomberos.sgib.domain.model

/**
 * Modelo de dominio para Bombero
 */
data class Bombero(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val rango: String,
    val especialidad: String?,
    val estado: String, // "Activo", "Licencia", "Inactivo"
    val telefono: String?,
    val email: String?,
    val direccion: String?,
    val fechaIngreso: String?,
    val fotoUrl: String?,
    val createdAt: String,
    val updatedAt: String
) {
    val nombreCompleto: String
        get() = "$nombres $apellidos"

    val isActivo: Boolean
        get() = estado == "Activo"
}

