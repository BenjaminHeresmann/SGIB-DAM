package com.bomberos.sgib.data.repository

import com.bomberos.sgib.data.remote.ApiService
import com.bomberos.sgib.data.remote.dto.CitacionCreateRequest
import com.bomberos.sgib.data.remote.dto.CitacionUpdateRequest
import com.bomberos.sgib.data.remote.dto.toDomain
import com.bomberos.sgib.domain.model.Citacion
import com.bomberos.sgib.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Repositorio para gestionar citaciones
 */
interface CitacionRepository {
    fun getCitaciones(
        estado: String? = null,
        tipoActividad: String? = null
    ): Flow<Resource<List<Citacion>>>

    fun getCitacionById(id: String): Flow<Resource<Citacion>>

    fun createCitacion(request: CitacionCreateRequest): Flow<Resource<Citacion>>

    fun updateCitacion(id: String, request: CitacionUpdateRequest): Flow<Resource<Citacion>>

    fun deleteCitacion(id: String): Flow<Resource<Boolean>>

    fun confirmarAsistencia(id: String): Flow<Resource<Citacion>>

    fun rechazarAsistencia(id: String): Flow<Resource<Citacion>>
}

class CitacionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CitacionRepository {

    override fun getCitaciones(
        estado: String?,
        tipoActividad: String?
    ): Flow<Resource<List<Citacion>>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getCitaciones(
                estado = estado,
                tipoActividad = tipoActividad
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val citaciones = body.data.map { it.toDomain() }
                    emit(Resource.Success(citaciones))
                } else {
                    emit(Resource.Error(body?.message ?: "Error desconocido"))
                }
            } else {
                emit(Resource.Error("Error al obtener citaciones: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión."))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    override fun getCitacionById(id: String): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getCitacionById(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    emit(Resource.Success(body.data.toDomain()))
                } else {
                    emit(Resource.Error(body?.message ?: "Citación no encontrada"))
                }
            } else {
                emit(Resource.Error("Error al obtener citación: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión."))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    override fun createCitacion(request: CitacionCreateRequest): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.createCitacion(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    emit(Resource.Success(body.data.toDomain()))
                } else {
                    emit(Resource.Error(body?.message ?: "Error al crear citación"))
                }
            } else {
                emit(Resource.Error("Error al crear citación: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión."))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    override fun updateCitacion(
        id: String,
        request: CitacionUpdateRequest
    ): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.updateCitacion(id, request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    emit(Resource.Success(body.data.toDomain()))
                } else {
                    emit(Resource.Error(body?.message ?: "Error al actualizar citación"))
                }
            } else {
                emit(Resource.Error("Error al actualizar citación: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión."))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    override fun deleteCitacion(id: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.deleteCitacion(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    emit(Resource.Success(true))
                } else {
                    emit(Resource.Error(body?.message ?: "Error al eliminar citación"))
                }
            } else {
                emit(Resource.Error("Error al eliminar citación: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión."))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    override fun confirmarAsistencia(id: String): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.confirmarAsistencia(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    emit(Resource.Success(body.data.toDomain()))
                } else {
                    emit(Resource.Error(body?.message ?: "Error al confirmar asistencia"))
                }
            } else {
                emit(Resource.Error("Error al confirmar asistencia: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión."))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    override fun rechazarAsistencia(id: String): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.rechazarAsistencia(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    emit(Resource.Success(body.data.toDomain()))
                } else {
                    emit(Resource.Error(body?.message ?: "Error al rechazar asistencia"))
                }
            } else {
                emit(Resource.Error("Error al rechazar asistencia: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red. Verifica tu conexión."))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
}

