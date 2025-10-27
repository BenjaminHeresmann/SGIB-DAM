package com.bomberos.sgib.domain.model

import java.time.LocalDateTime

/**
 * Modelo de dominio para una Citación
 */
data class Citacion(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val fecha: LocalDateTime,
    val lugar: String,
    val tipoActividad: TipoActividad,
    val estado: EstadoCitacion,
    val asistentesRequeridos: Int,
    val asistentesConfirmados: Int,
    val bomberosCitados: List<Int> = emptyList(),
    val creadoPor: String,
    val fechaCreacion: LocalDateTime,
    val observaciones: String? = null
)

/**
 * Tipos de actividades para las citaciones
 */
enum class TipoActividad(val displayName: String) {
    ENTRENAMIENTO("Entrenamiento"),
    GUARDIA("Guardia"),
    REUNION("Reunión"),
    CEREMONIA("Ceremonia"),
    EJERCICIO("Ejercicio"),
    OTRO("Otro")
}

/**
 * Estados posibles de una citación
 */
enum class EstadoCitacion(val displayName: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    EN_CURSO("En Curso"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada")
}

