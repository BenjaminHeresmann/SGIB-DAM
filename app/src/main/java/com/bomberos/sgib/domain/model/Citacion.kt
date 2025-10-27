package com.bomberos.sgib.domain.model

import java.time.LocalDateTime

// Modelo de dominio para una Citacion
// Una citacion es una convocatoria oficial a bomberos para una actividad especifica
// Puede ser un entrenamiento, guardia, reunion, ceremonia, etc.
// Este modelo gestiona toda la informacion relacionada con la citacion

data class Citacion(
    // ID unico de la citacion en la base de datos
    // Se genera automaticamente al crear una citacion nueva
    val id: Int,

    // Titulo descriptivo de la citacion
    // Ejemplo: "Entrenamiento de Rescate Vehicular", "Guardia Nocturna Enero"
    // Campo obligatorio, aparece en listas y notificaciones
    val titulo: String,

    // Descripcion detallada de la citacion
    // Explica el proposito, actividades a realizar, equipamiento necesario, etc.
    // Campo obligatorio, proporciona contexto completo de la citacion
    val descripcion: String,

    // Fecha y hora programada de la citacion
    // Tipo LocalDateTime: incluye fecha (ano, mes, dia) y hora (hora, minuto, segundo)
    // Usado para ordenar citaciones, enviar recordatorios y validar conflictos
    val fecha: LocalDateTime,

    // Lugar donde se realizara la citacion
    // Puede ser la estacion de bomberos, lugar de entrenamiento, direccion especifica
    // Ejemplo: "Estacion Central", "Campo de Entrenamiento Norte"
    val lugar: String,

    // Tipo de actividad de la citacion
    // Es un enum que define categorias predefinidas
    // Usado para filtrar, clasificar y generar estadisticas de citaciones
    val tipoActividad: TipoActividad,

    // Estado actual de la citacion en su ciclo de vida
    // Es un enum que define los estados posibles
    // Permite trackear el progreso desde la creacion hasta la finalizacion
    val estado: EstadoCitacion,

    // Numero de bomberos que se necesitan para la citacion
    // Define el cupo minimo o ideal de asistentes
    // Usado para validar si hay suficientes confirmaciones
    val asistentesRequeridos: Int,

    // Numero de bomberos que han confirmado asistencia
    // Se incrementa cuando un bombero acepta la citacion
    // Usado para mostrar progreso: "15/20 confirmados"
    val asistentesConfirmados: Int,

    // Lista de IDs de bomberos citados para esta actividad
    // Contiene los IDs de los bomberos que fueron convocados
    // Lista vacia por defecto si no se especifica
    // Usado para mostrar quienes deben asistir y enviar notificaciones
    val bomberosCitados: List<Int> = emptyList(),

    // Nombre o ID del usuario que creo la citacion
    // Identifica quien genero la convocatoria para auditoria y responsabilidad
    val creadoPor: String,

    // Fecha y hora en que se creo la citacion en el sistema
    // Usado para ordenar citaciones por fecha de creacion y auditoria
    val fechaCreacion: LocalDateTime,

    // Observaciones o notas adicionales sobre la citacion
    // Campo opcional para informacion complementaria
    // Tipo String?: puede ser null si no hay observaciones
    val observaciones: String? = null
)

// Enum TipoActividad: Define los tipos de actividades que puede tener una citacion
// Un enum es un tipo especial que solo puede tener valores predefinidos
// Cada valor tiene un displayName para mostrar en la interfaz de usuario
enum class TipoActividad(val displayName: String) {
    // Actividad de capacitacion y practica
    ENTRENAMIENTO("Entrenamiento"),

    // Turno de vigilancia en la estacion
    GUARDIA("Guardia"),

    // Junta administrativa o coordinacion
    REUNION("Reunión"),

    // Evento oficial o protocolar
    CEREMONIA("Ceremonia"),

    // Simulacro o practica especifica
    EJERCICIO("Ejercicio"),

    // Cualquier otro tipo de actividad no categorizada
    OTRO("Otro")
}
// El enum TipoActividad se usa en:
// - CitacionesScreen: para filtrar citaciones por tipo
// - CitacionDetalleScreen: para mostrar el tipo de actividad
// - Formularios de creacion: para seleccionar el tipo de citacion

// Enum EstadoCitacion: Define los estados posibles en el ciclo de vida de una citacion
// Permite trackear el progreso desde la creacion hasta el cierre
enum class EstadoCitacion(val displayName: String) {
    // Citacion creada pero aun no confirmada por todos los bomberos
    PENDIENTE("Pendiente"),

    // Citacion confirmada con suficientes asistentes
    CONFIRMADA("Confirmada"),

    // Citacion que esta ocurriendo en este momento
    EN_CURSO("En Curso"),

    // Citacion que ya finalizo y se cerro correctamente
    COMPLETADA("Completada"),

    // Citacion que fue cancelada antes de realizarse
    CANCELADA("Cancelada")
}
// El enum EstadoCitacion se usa en:
// - CitacionesScreen: para filtrar citaciones por estado y mostrar con colores diferentes
// - CitacionDetalleScreen: para mostrar el estado actual y permitir cambios de estado
// - DashboardScreen: para contar citaciones pendientes y completadas

// Este modelo se conecta con:
// - CitacionesScreen: lista todas las citaciones
// - CitacionDetalleScreen: muestra detalle completo de una citacion
// - CitacionRepository: maneja operaciones CRUD de citaciones
// - FakeDataSource: almacena las citaciones en memoria


