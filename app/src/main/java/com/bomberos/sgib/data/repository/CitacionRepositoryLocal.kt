package com.bomberos.sgib.data.repository

import com.bomberos.sgib.data.remote.dto.CitacionCreateRequest
import com.bomberos.sgib.data.remote.dto.CitacionUpdateRequest
import com.bomberos.sgib.domain.model.Citacion
import com.bomberos.sgib.domain.model.TipoActividad
import com.bomberos.sgib.domain.model.EstadoCitacion
import com.bomberos.sgib.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Repositorio local de citaciones (simulado) para desarrollo sin backend
 */
class CitacionRepositoryLocal @Inject constructor() : CitacionRepository {

    private val citaciones = mutableListOf(
        Citacion(
            id = 1,
            titulo = "Entrenamiento de Rescate",
            descripcion = "Práctica de técnicas de rescate en altura y espacios confinados",
            fecha = LocalDateTime.now().plusDays(3),
            lugar = "Cuartel Central - Segunda Compañía",
            tipoActividad = TipoActividad.ENTRENAMIENTO,
            estado = EstadoCitacion.PENDIENTE,
            asistentesRequeridos = 15,
            asistentesConfirmados = 8,
            bomberosCitados = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
            creadoPor = "Cap. Juan Pérez",
            fechaCreacion = LocalDateTime.now().minusDays(2),
            observaciones = "Traer equipo completo y uniforme de trabajo"
        ),
        Citacion(
            id = 2,
            titulo = "Guardia Nocturna",
            descripcion = "Turno de guardia nocturna en el cuartel",
            fecha = LocalDateTime.now().plusDays(1),
            lugar = "Cuartel Central",
            tipoActividad = TipoActividad.GUARDIA,
            estado = EstadoCitacion.CONFIRMADA,
            asistentesRequeridos = 8,
            asistentesConfirmados = 8,
            bomberosCitados = listOf(2, 3, 5, 7, 9, 10, 12, 14),
            creadoPor = "Tte. Carlos Ramírez",
            fechaCreacion = LocalDateTime.now().minusDays(5),
            observaciones = null
        ),
        Citacion(
            id = 3,
            titulo = "Reunión Mensual",
            descripcion = "Reunión administrativa mensual de la compañía",
            fecha = LocalDateTime.now().plusDays(7),
            lugar = "Sala de Juntas",
            tipoActividad = TipoActividad.REUNION,
            estado = EstadoCitacion.PENDIENTE,
            asistentesRequeridos = 10,
            asistentesConfirmados = 3,
            bomberosCitados = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            creadoPor = "Cmd. Roberto Silva",
            fechaCreacion = LocalDateTime.now().minusDays(1),
            observaciones = "Traer informes del mes"
        ),
        Citacion(
            id = 4,
            titulo = "Ceremonia del Día del Bombero",
            descripcion = "Ceremonia oficial en conmemoración del Día del Bombero Chileno",
            fecha = LocalDateTime.now().plusDays(10),
            lugar = "Plaza de Armas",
            tipoActividad = TipoActividad.CEREMONIA,
            estado = EstadoCitacion.PENDIENTE,
            asistentesRequeridos = 30,
            asistentesConfirmados = 12,
            bomberosCitados = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            creadoPor = "Cmd. Roberto Silva",
            fechaCreacion = LocalDateTime.now().minusDays(7),
            observaciones = "Uniforme de gala. Formación a las 09:00 hrs."
        ),
        Citacion(
            id = 5,
            titulo = "Ejercicio de Evacuación",
            descripcion = "Simulacro de evacuación en edificio de gran altura",
            fecha = LocalDateTime.now().minusDays(2),
            lugar = "Edificio Torre Norte",
            tipoActividad = TipoActividad.EJERCICIO,
            estado = EstadoCitacion.COMPLETADA,
            asistentesRequeridos = 12,
            asistentesConfirmados = 12,
            bomberosCitados = listOf(1, 3, 5, 7, 9, 11, 13, 15, 2, 4, 6, 8),
            creadoPor = "Cap. Juan Pérez",
            fechaCreacion = LocalDateTime.now().minusDays(10),
            observaciones = "Coordinación con Carabineros y SAMU"
        )
    )

    private var nextId = 6

    override fun getCitaciones(
        estado: String?,
        tipoActividad: String?
    ): Flow<Resource<List<Citacion>>> = flow {
        try {
            emit(Resource.Loading())
            delay(500) // Simular latencia de red

            var filtered = citaciones.toList()

            // Filtrar por estado
            if (estado != null) {
                filtered = filtered.filter { it.estado.name == estado }
            }

            // Filtrar por tipo de actividad
            if (tipoActividad != null) {
                filtered = filtered.filter { it.tipoActividad.name == tipoActividad }
            }

            // Ordenar por fecha descendente (más recientes primero)
            filtered = filtered.sortedByDescending { it.fecha }

            emit(Resource.Success(filtered))
        } catch (e: Exception) {
            emit(Resource.Error("Error al cargar citaciones: ${e.localizedMessage}"))
        }
    }

    override fun getCitacionById(id: Int): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            delay(300)

            val citacion = citaciones.find { it.id == id }
            if (citacion != null) {
                emit(Resource.Success(citacion))
            } else {
                emit(Resource.Error("Citación no encontrada"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error al cargar citación: ${e.localizedMessage}"))
        }
    }

    override fun createCitacion(request: CitacionCreateRequest): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            delay(500)

            val nuevaCitacion = Citacion(
                id = nextId++,
                titulo = request.titulo,
                descripcion = request.descripcion,
                fecha = LocalDateTime.parse(request.fecha),
                lugar = request.lugar,
                tipoActividad = TipoActividad.valueOf(request.tipoActividad),
                estado = EstadoCitacion.PENDIENTE,
                asistentesRequeridos = request.asistentesRequeridos,
                asistentesConfirmados = 0,
                bomberosCitados = request.bomberosCitados,
                creadoPor = "Usuario Actual",
                fechaCreacion = LocalDateTime.now(),
                observaciones = request.observaciones
            )

            citaciones.add(nuevaCitacion)
            emit(Resource.Success(nuevaCitacion))
        } catch (e: Exception) {
            emit(Resource.Error("Error al crear citación: ${e.localizedMessage}"))
        }
    }

    override fun updateCitacion(
        id: Int,
        request: CitacionUpdateRequest
    ): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            delay(500)

            val index = citaciones.indexOfFirst { it.id == id }
            if (index != -1) {
                val citacionActual = citaciones[index]
                val citacionActualizada = citacionActual.copy(
                    titulo = request.titulo ?: citacionActual.titulo,
                    descripcion = request.descripcion ?: citacionActual.descripcion,
                    fecha = request.fecha?.let { LocalDateTime.parse(it) } ?: citacionActual.fecha,
                    lugar = request.lugar ?: citacionActual.lugar,
                    tipoActividad = request.tipoActividad?.let { TipoActividad.valueOf(it) } ?: citacionActual.tipoActividad,
                    estado = request.estado?.let { EstadoCitacion.valueOf(it) } ?: citacionActual.estado,
                    asistentesRequeridos = request.asistentesRequeridos ?: citacionActual.asistentesRequeridos,
                    bomberosCitados = request.bomberosCitados ?: citacionActual.bomberosCitados,
                    observaciones = request.observaciones
                )

                citaciones[index] = citacionActualizada
                emit(Resource.Success(citacionActualizada))
            } else {
                emit(Resource.Error("Citación no encontrada"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error al actualizar citación: ${e.localizedMessage}"))
        }
    }

    override fun deleteCitacion(id: Int): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            delay(500)

            val removed = citaciones.removeIf { it.id == id }
            if (removed) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Citación no encontrada"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error al eliminar citación: ${e.localizedMessage}"))
        }
    }

    override fun confirmarAsistencia(id: Int): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            delay(300)

            val index = citaciones.indexOfFirst { it.id == id }
            if (index != -1) {
                val citacionActual = citaciones[index]
                val citacionActualizada = citacionActual.copy(
                    asistentesConfirmados = citacionActual.asistentesConfirmados + 1
                )

                citaciones[index] = citacionActualizada
                emit(Resource.Success(citacionActualizada))
            } else {
                emit(Resource.Error("Citación no encontrada"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error al confirmar asistencia: ${e.localizedMessage}"))
        }
    }

    override fun rechazarAsistencia(id: Int): Flow<Resource<Citacion>> = flow {
        try {
            emit(Resource.Loading())
            delay(300)

            val index = citaciones.indexOfFirst { it.id == id }
            if (index != -1) {
                val citacion = citaciones[index]
                emit(Resource.Success(citacion))
            } else {
                emit(Resource.Error("Citación no encontrada"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error al rechazar asistencia: ${e.localizedMessage}"))
        }
    }
}

