package com.bomberos.sgib.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Bombero desde API
 */
data class BomberoDto(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val rango: String,
    val especialidad: String?,
    val estado: String,
    val telefono: String?,
    val email: String?,
    val direccion: String?,
    @SerializedName("fechaIngreso")
    val fechaIngreso: String?,
    @SerializedName("fotoUrl")
    val fotoUrl: String?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

/**
 * Response para lista de bomberos con paginación
 */
data class BomberosResponse(
    val success: Boolean,
    val data: List<BomberoDto>,
    val pagination: PaginationDto
)

/**
 * Response para un bombero individual
 */
data class BomberoResponse(
    val success: Boolean,
    val data: BomberoDto,
    val message: String?
)

/**
 * DTO para paginación
 */
data class PaginationDto(
    val current: Int,
    val pages: Int,
    val total: Int,
    val hasNext: Boolean,
    val hasPrev: Boolean
)

/**
 * Request para crear/actualizar bombero
 */
data class BomberoRequest(
    val nombres: String,
    val apellidos: String,
    val rango: String,
    val especialidad: String? = null,
    val estado: String = "Activo",
    val telefono: String? = null,
    val email: String? = null,
    val direccion: String? = null,
    val fechaIngreso: String? = null,
    val fotoUrl: String? = null
)

