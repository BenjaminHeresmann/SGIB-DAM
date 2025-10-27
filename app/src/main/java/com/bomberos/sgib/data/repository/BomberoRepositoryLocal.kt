package com.bomberos.sgib.data.repository

import com.bomberos.sgib.data.local.FakeDataSource
import com.bomberos.sgib.domain.model.Bombero
import com.bomberos.sgib.domain.model.Stats
import com.bomberos.sgib.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Repositorio para gestión de bomberos
 * VERSIÓN SIN BACKEND - Usa datos locales simulados
 */
class BomberoRepositoryLocal @Inject constructor() {

    /**
     * Obtener lista de bomberos con filtros (sin backend)
     */
    fun getBomberos(
        page: Int = 1,
        search: String = "",
        estado: String = "Activo"
    ): Flow<Resource<List<Bombero>>> = flow {
        try {
            emit(Resource.Loading())

            // Simular delay de red
            delay(500)

            // Obtener bomberos filtrados desde datos locales
            val bomberos = if (search.isNotBlank()) {
                FakeDataSource.searchBomberos(search)
            } else if (estado == "Todos" || estado.isBlank()) {
                FakeDataSource.getAllBomberos()
            } else {
                FakeDataSource.getBomberosByEstado(estado)
            }

            emit(Resource.Success(bomberos))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    /**
     * Obtener bombero por ID (sin backend)
     */
    fun getBomberoById(id: Int): Flow<Resource<Bombero>> = flow {
        try {
            emit(Resource.Loading())

            // Simular delay
            delay(300)

            val bombero = FakeDataSource.getBomberoById(id)
            if (bombero != null) {
                emit(Resource.Success(bombero))
            } else {
                emit(Resource.Error("Bombero no encontrado"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    /**
     * Obtener estadísticas (sin backend)
     */
    fun getStats(): Flow<Resource<Stats>> = flow {
        try {
            emit(Resource.Loading())

            // Simular delay
            delay(500)

            val stats = FakeDataSource.getStats()
            emit(Resource.Success(stats))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    /**
     * Crear un nuevo bombero
     */
    fun createBombero(bombero: Bombero): Flow<Resource<Bombero>> = flow {
        try {
            emit(Resource.Loading())

            // Simular delay de red
            delay(500)

            val newBombero = FakeDataSource.createBombero(bombero)
            emit(Resource.Success(newBombero))
        } catch (e: Exception) {
            emit(Resource.Error("Error al crear bombero: ${e.localizedMessage}"))
        }
    }

    /**
     * Actualizar un bombero existente
     */
    fun updateBombero(bombero: Bombero): Flow<Resource<Bombero>> = flow {
        try {
            emit(Resource.Loading())

            // Simular delay de red
            delay(500)

            val updatedBombero = FakeDataSource.updateBombero(bombero)
            if (updatedBombero != null) {
                emit(Resource.Success(updatedBombero))
            } else {
                emit(Resource.Error("Bombero no encontrado"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error al actualizar bombero: ${e.localizedMessage}"))
        }
    }

    /**
     * Eliminar un bombero
     */
    fun deleteBombero(id: Int): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())

            // Simular delay de red
            delay(500)

            val deleted = FakeDataSource.deleteBombero(id)
            if (deleted) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Bombero no encontrado"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error al eliminar bombero: ${e.localizedMessage}"))
        }
    }
}
