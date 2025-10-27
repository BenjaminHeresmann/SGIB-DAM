package com.bomberos.sgib.data.local

import com.bomberos.sgib.domain.model.Bombero
import com.bomberos.sgib.domain.model.Stats
import com.bomberos.sgib.domain.model.RangoCount
import com.bomberos.sgib.domain.model.User

/**
 * Fuente de datos falsa/simulada para trabajar SIN backend
 * Datos precargados localmente
 */
object FakeDataSource {

    // Usuario por defecto
    val defaultUser = User(
        id = 1,
        email = "admin",
        nombre = "Administrador del Sistema",
        rol = "Comandante",
        tipo = "admin",
        activo = true
    )

    val userBombero = User(
        id = 2,
        email = "bombero@bomberos.cl",
        nombre = "Usuario Bombero",
        rol = "Bombero",
        tipo = "usuario",
        activo = true
    )

    // Lista de bomberos precargados (mutable para permitir CRUD)
    private val bomberosList = mutableListOf(
        Bombero(
            id = 1,
            nombres = "Juan Carlos",
            apellidos = "González Muñoz",
            rango = "Comandante",
            especialidad = "Rescate Vehicular",
            estado = "Activo",
            telefono = "+56 9 1234 5678",
            email = "juan.gonzalez@bomberos.cl",
            direccion = "Av. Libertad 123, Viña del Mar",
            fechaIngreso = "2015-03-15",
            fotoUrl = "/assets/bomberos/bombero-1.jpg",
            createdAt = "2015-03-15T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        ),
        Bombero(
            id = 2,
            nombres = "María Paz",
            apellidos = "Rojas Silva",
            rango = "Capitán",
            especialidad = "Materiales Peligrosos",
            estado = "Activo",
            telefono = "+56 9 2345 6789",
            email = "maria.rojas@bomberos.cl",
            direccion = "Calle Valparaíso 456, Viña del Mar",
            fechaIngreso = "2016-07-20",
            fotoUrl = "/assets/bomberos/bombero-2.jpg",
            createdAt = "2016-07-20T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        ),
        Bombero(
            id = 3,
            nombres = "Pedro Antonio",
            apellidos = "Díaz Pérez",
            rango = "Teniente",
            especialidad = "Incendios Forestales",
            estado = "Activo",
            telefono = "+56 9 3456 7890",
            email = "pedro.diaz@bomberos.cl",
            direccion = "Pasaje Los Héroes 789, Viña del Mar",
            fechaIngreso = "2017-11-05",
            fotoUrl = "/assets/bomberos/bombero-3.jpg",
            createdAt = "2017-11-05T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        ),
        Bombero(
            id = 4,
            nombres = "Ana Isabel",
            apellidos = "Soto Contreras",
            rango = "Sargento",
            especialidad = "Primeros Auxilios",
            estado = "Activo",
            telefono = "+56 9 4567 8901",
            email = "ana.soto@bomberos.cl",
            direccion = "Av. Marina 321, Viña del Mar",
            fechaIngreso = "2018-02-14",
            fotoUrl = "/assets/bomberos/bombero-4.jpg",
            createdAt = "2018-02-14T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        ),
        Bombero(
            id = 5,
            nombres = "Luis Fernando",
            apellidos = "Martínez López",
            rango = "Cabo",
            especialidad = "Rescate en Altura",
            estado = "Activo",
            telefono = "+56 9 5678 9012",
            email = "luis.martinez@bomberos.cl",
            direccion = "Calle Quillota 654, Viña del Mar",
            fechaIngreso = "2019-06-30",
            fotoUrl = "/assets/bomberos/bombero-5.jpg",
            createdAt = "2019-06-30T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        ),
        Bombero(
            id = 6,
            nombres = "Carolina Andrea",
            apellidos = "Sepúlveda Morales",
            rango = "Bombero",
            especialidad = null,
            estado = "Activo",
            telefono = "+56 9 6789 0123",
            email = "carolina.sepulveda@bomberos.cl",
            direccion = "Pasaje Los Robles 987, Viña del Mar",
            fechaIngreso = "2020-09-12",
            fotoUrl = "/assets/bomberos/bombero-6.jpg",
            createdAt = "2020-09-12T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        ),
        Bombero(
            id = 7,
            nombres = "Roberto Carlos",
            apellidos = "Rodríguez Torres",
            rango = "Bombero",
            especialidad = null,
            estado = "Licencia",
            telefono = "+56 9 7890 1234",
            email = "roberto.rodriguez@bomberos.cl",
            direccion = "Av. España 147, Viña del Mar",
            fechaIngreso = "2021-01-25",
            fotoUrl = "/assets/bomberos/bombero-7.jpg",
            createdAt = "2021-01-25T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        ),
        Bombero(
            id = 8,
            nombres = "Patricia Elena",
            apellidos = "Fuentes Hernández",
            rango = "Bombero",
            especialidad = null,
            estado = "Activo",
            telefono = "+56 9 8901 2345",
            email = "patricia.fuentes@bomberos.cl",
            direccion = "Calle Arlegui 258, Viña del Mar",
            fechaIngreso = "2022-04-18",
            fotoUrl = "/assets/bomberos/bombero-8.jpg",
            createdAt = "2022-04-18T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        ),
        Bombero(
            id = 9,
            nombres = "Diego Alejandro",
            apellidos = "Flores Espinoza",
            rango = "Bombero",
            especialidad = null,
            estado = "Activo",
            telefono = "+56 9 9012 3456",
            email = "diego.flores@bomberos.cl",
            direccion = "Pasaje San Martín 369, Viña del Mar",
            fechaIngreso = "2023-08-07",
            fotoUrl = "/assets/bomberos/bombero-1.jpg",
            createdAt = "2023-08-07T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        ),
        Bombero(
            id = 10,
            nombres = "Valentina Sofía",
            apellidos = "Valenzuela Castillo",
            rango = "Bombero",
            especialidad = null,
            estado = "Inactivo",
            telefono = "+56 9 0123 4567",
            email = "valentina.valenzuela@bomberos.cl",
            direccion = "Av. Agua Santa 741, Viña del Mar",
            fechaIngreso = "2024-02-20",
            fotoUrl = "/assets/bomberos/bombero-2.jpg",
            createdAt = "2024-02-20T10:00:00",
            updatedAt = "2025-10-24T10:00:00"
        )
    )

    // Estadísticas calculadas
    fun getStats(): Stats {
        val activos = bomberosList.count { it.estado == "Activo" }
        val licencia = bomberosList.count { it.estado == "Licencia" }
        val inactivos = bomberosList.count { it.estado == "Inactivo" }

        val porRango = bomberosList
            .groupBy { it.rango }
            .map { (rango, list) -> RangoCount(rango, list.size) }
            .sortedByDescending { it.cantidad }

        // Calcular bomberos nuevos del último mes (aproximado)
        val fechaHaceUnMes = java.time.LocalDateTime.now().minusMonths(1)
        val nuevosUltimoMes = try {
            bomberosList.count { bombero ->
                try {
                    val createdAt = java.time.LocalDateTime.parse(bombero.createdAt)
                    createdAt.isAfter(fechaHaceUnMes)
                } catch (e: Exception) {
                    false
                }
            }
        } catch (e: Exception) {
            0
        }

        return Stats(
            totalActivos = activos,
            totalInactivos = inactivos + licencia,
            total = bomberosList.size,
            porRango = porRango,
            nuevosUltimoMes = nuevosUltimoMes
        )
    }

    // Obtener todos los bomberos
    fun getAllBomberos(): List<Bombero> = bomberosList

    // Obtener bomberos filtrados por estado
    fun getBomberosByEstado(estado: String): List<Bombero> {
        return if (estado.isBlank() || estado == "Todos") {
            bomberosList
        } else {
            bomberosList.filter { it.estado == estado }
        }
    }

    // Obtener bombero por ID
    fun getBomberoById(id: Int): Bombero? {
        return bomberosList.find { it.id == id }
    }

    // Buscar bomberos
    fun searchBomberos(query: String): List<Bombero> {
        if (query.isBlank()) return bomberosList

        return bomberosList.filter {
            it.nombreCompleto.contains(query, ignoreCase = true) ||
            it.rango.contains(query, ignoreCase = true) ||
            it.especialidad?.contains(query, ignoreCase = true) == true
        }
    }

    // Validar credenciales (simulado)
    fun validateCredentials(email: String, password: String): User? {
        return when {
            email == "admin" && password == "1234" -> defaultUser
            email == "bombero@bomberos.cl" && password == "bomb345" -> userBombero
            else -> null
        }
    }

    // ==================== MÉTODOS CRUD ====================

    /**
     * Crear un nuevo bombero
     */
    fun createBombero(bombero: Bombero): Bombero {
        // Generar nuevo ID
        val newId = (bomberosList.maxOfOrNull { it.id } ?: 0) + 1

        // Crear bombero con ID generado y timestamps
        val now = java.time.LocalDateTime.now().toString()
        val newBombero = bombero.copy(
            id = newId,
            createdAt = now,
            updatedAt = now
        )

        // Agregar a la lista
        bomberosList.add(newBombero)

        return newBombero
    }

    /**
     * Actualizar un bombero existente
     */
    fun updateBombero(bombero: Bombero): Bombero? {
        val index = bomberosList.indexOfFirst { it.id == bombero.id }

        if (index != -1) {
            // Actualizar timestamp
            val now = java.time.LocalDateTime.now().toString()
            val updatedBombero = bombero.copy(updatedAt = now)

            bomberosList[index] = updatedBombero
            return updatedBombero
        }

        return null
    }

    /**
     * Eliminar un bombero
     */
    fun deleteBombero(id: Int): Boolean {
        return bomberosList.removeIf { it.id == id }
    }
}
