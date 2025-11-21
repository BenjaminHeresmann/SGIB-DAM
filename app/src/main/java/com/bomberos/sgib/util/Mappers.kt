package com.bomberos.sgib.util

import com.bomberos.sgib.data.remote.dto.*
import com.bomberos.sgib.domain.model.*

/**
 * Mappers para convertir DTOs en modelos de dominio
 */

// User
fun UserDto.toUser(): User {
    return User(
        id = id,
        email = email,
        nombre = nombre,
        rol = rol,
        tipo = tipo,
        activo = activo
    )
}

// Bombero
fun BomberoDto.toBombero(): Bombero {
    return Bombero(
        id = id,
        nombres = nombres,
        apellidos = apellidos,
        rango = rango,
        especialidad = especialidad,
        estado = estado,
        telefono = telefono,
        email = email,
        direccion = direccion,
        fechaIngreso = fechaIngreso,
        fotoUrl = fotoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// Stats
fun StatsDto.toStats(): Stats {
    return Stats(
        totalBomberos = totalBomberos,
        totalActivos = totalActivos,
        totalInactivos = totalInactivos,
        totalSuspendidos = totalSuspendidos,
        totalBajas = totalBajas,
        totalRenuncias = totalRenuncias,
        totalNoActivos = totalNoActivos,
        porcentajeActivos = porcentajeActivos
    )
}

// Bombero to Request
fun Bombero.toBomberoRequest(): BomberoRequest {
    return BomberoRequest(
        nombres = nombres,
        apellidos = apellidos,
        rango = rango,
        especialidad = especialidad,
        estado = estado,
        telefono = telefono,
        email = email,
        direccion = direccion,
        fechaIngreso = fechaIngreso,
        fotoUrl = fotoUrl
    )
}

