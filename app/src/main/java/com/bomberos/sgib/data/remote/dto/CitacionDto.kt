package com.bomberos.sgib.data.remote.dto

import com.bomberos.sgib.domain.model.Citacion
import com.bomberos.sgib.domain.model.TipoActividad
import com.bomberos.sgib.domain.model.EstadoCitacion
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * DTOs para el módulo de citaciones
 */
data class CitacionDto(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val lugar: String,
    val tipoActividad: String,
    val estado: String,
    val asistentesRequeridos: Int,
    val asistentesConfirmados: Int,
    val bomberosCitados: List<Int> = emptyList(),
    val creadoPor: String,
    val fechaCreacion: String,
    val observaciones: String? = null
)

fun CitacionDto.toDomain(): Citacion {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    return Citacion(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        fecha = LocalDateTime.parse(fecha, formatter),
        lugar = lugar,
        tipoActividad = TipoActividad.valueOf(tipoActividad),
        estado = EstadoCitacion.valueOf(estado),
        asistentesRequeridos = asistentesRequeridos,
        asistentesConfirmados = asistentesConfirmados,
        bomberosCitados = bomberosCitados,
        creadoPor = creadoPor,
        fechaCreacion = LocalDateTime.parse(fechaCreacion, formatter),
        observaciones = observaciones
    )
}

data class CitacionCreateRequest(
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val lugar: String,
    val tipoActividad: String,
    val asistentesRequeridos: Int,
    val bomberosCitados: List<Int>,
    val observaciones: String? = null
)

data class CitacionUpdateRequest(
    val titulo: String?,
    val descripcion: String?,
    val fecha: String?,
    val lugar: String?,
    val tipoActividad: String?,
    val estado: String?,
    val asistentesRequeridos: Int?,
    val bomberosCitados: List<Int>?,
    val observaciones: String?
)

data class CitacionResponse(
    val success: Boolean,
    val message: String,
    val data: CitacionDto?
)

data class CitacionListResponse(
    val success: Boolean,
    val message: String,
    val data: List<CitacionDto>
)

