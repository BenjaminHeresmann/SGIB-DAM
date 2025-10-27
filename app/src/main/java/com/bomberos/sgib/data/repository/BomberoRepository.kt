package com.bomberos.sgib.data.repository

import com.bomberos.sgib.data.remote.ApiService
import com.bomberos.sgib.data.remote.dto.BomberoRequest
import com.bomberos.sgib.domain.model.Bombero
import com.bomberos.sgib.domain.model.Stats
import com.bomberos.sgib.util.toBombero
import com.bomberos.sgib.util.toStats
import com.bomberos.sgib.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Repositorio para gestión de bomberos
 */
class BomberoRepository @Inject constructor(
    private val api: ApiService
) {

    /**
     * Obtener lista de bomberos con filtros
     */
    fun getBomberos(
        page: Int = 1,
        search: String = "",
        estado: String = "Activo"
    ): Flow<Resource<List<Bombero>>> = flow {
        try {
            emit(Resource.Loading())

            val response = api.getBomberos(
                page = page,
                limit = 20,
                search = search,
                estado = if (estado == "Todos") "" else estado
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val bomberos = response.body()?.data?.map { it.toBombero() } ?: emptyList()
                emit(Resource.Success(bomberos))
            } else {
                emit(Resource.Error("Error al cargar bomberos"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    /**
     * Obtener bombero por ID
     */
    fun getBomberoById(id: Int): Flow<Resource<Bombero>> = flow {
        try {
            emit(Resource.Loading())

            val response = api.getBomberoById(id)

            if (response.isSuccessful && response.body()?.success == true) {
                val bombero = response.body()?.data?.toBombero()
                if (bombero != null) {
                    emit(Resource.Success(bombero))
                } else {
                    emit(Resource.Error("Bombero no encontrado"))
                }
            } else {
                emit(Resource.Error("Error al cargar bombero"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    /**
     * Crear nuevo bombero
     */
    fun createBombero(request: BomberoRequest): Flow<Resource<Bombero>> = flow {
        try {
            emit(Resource.Loading())

            val response = api.createBombero(request)

            if (response.isSuccessful && response.body()?.success == true) {
                val bombero = response.body()?.data?.toBombero()
                if (bombero != null) {
                    emit(Resource.Success(bombero))
                } else {
                    emit(Resource.Error("Error al crear bombero"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Error al crear bombero"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    /**
     * Actualizar bombero
     */
    fun updateBombero(id: Int, request: BomberoRequest): Flow<Resource<Bombero>> = flow {
        try {
            emit(Resource.Loading())

            val response = api.updateBombero(id, request)

            if (response.isSuccessful && response.body()?.success == true) {
                val bombero = response.body()?.data?.toBombero()
                if (bombero != null) {
                    emit(Resource.Success(bombero))
                } else {
                    emit(Resource.Error("Error al actualizar bombero"))
                }
            } else {
                emit(Resource.Error("Error al actualizar bombero"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    /**
     * Eliminar bombero (soft delete)
     */
    fun deleteBombero(id: Int): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())

            val response = api.deleteBombero(id)

            if (response.isSuccessful && response.body()?.success == true) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Error al eliminar bombero"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    /**
     * Obtener estadísticas
     */
    fun getStats(): Flow<Resource<Stats>> = flow {
        try {
            emit(Resource.Loading())

            val response = api.getStats()

            if (response.isSuccessful && response.body()?.success == true) {
                val stats = response.body()?.data?.toStats()
                if (stats != null) {
                    emit(Resource.Success(stats))
                } else {
                    emit(Resource.Error("Error al cargar estadísticas"))
                }
            } else {
                emit(Resource.Error("Error al cargar estadísticas"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
}

