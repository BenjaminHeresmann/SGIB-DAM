package com.bomberos.sgib.domain.model

// Modelo de dominio para Bombero
// Representa la entidad principal de la aplicacion: un miembro del cuerpo de bomberos
// Contiene toda la informacion personal y profesional de un bombero
// Es una data class que se usa en toda la aplicacion para manipular datos de bomberos

data class Bombero(
    // ID unico del bombero en la base de datos
    // Se genera automaticamente al crear un bombero nuevo
    // Tipo Int: identificador numerico unico
    val id: Int,

    // Nombres del bombero (puede incluir primer y segundo nombre)
    // Campo obligatorio, no puede estar vacio
    val nombres: String,

    // Apellidos del bombero (apellido paterno y materno)
    // Campo obligatorio, no puede estar vacio
    val apellidos: String,

    // Rango jerarquico del bombero en la compania
    // Ejemplos: "Voluntario", "Bombero", "Cabo", "Sargento", "Teniente", "Capitan", "Comandante"
    // Define la posicion y responsabilidades del bombero
    val rango: String,

    // Especialidad o area de expertise del bombero
    // Ejemplos: "Rescate", "Materiales Peligrosos", "Primeros Auxilios", "Conductor"
    // Tipo String?: puede ser null si el bombero no tiene especialidad
    val especialidad: String?,

    // Estado actual del bombero en la compania
    // Valores posibles: "Activo", "Licencia", "Inactivo"
    // "Activo": en servicio regular
    // "Licencia": temporalmente ausente (medica, personal, etc.)
    // "Inactivo": no esta actualmente en servicio
    val estado: String,

    // Numero de telefono de contacto del bombero
    // Usado para comunicaciones de emergencia y coordinacion
    // Tipo String?: puede ser null si no se proporciona
    val telefono: String?,

    // Correo electronico del bombero
    // Usado para comunicaciones oficiales y notificaciones
    // Tipo String?: puede ser null si no se proporciona
    val email: String?,

    // Direccion residencial del bombero
    // Informacion opcional para registros administrativos
    // Tipo String?: puede ser null
    val direccion: String?,

    // Fecha en que el bombero ingreso a la compania
    // Formato esperado: "YYYY-MM-DD" o similar
    // Usado para calcular antiguedad y reconocimientos
    // Tipo String?: puede ser null si no se registra
    val fechaIngreso: String?,

    // URL de la foto del bombero
    // Puede ser una ruta local (file://) o URL remota (http://)
    // Usado para mostrar la foto en BomberoCard y pantallas de detalle
    // Tipo String?: puede ser null si no tiene foto
    val fotoUrl: String?,

    // Timestamp de cuando se creo el registro en la base de datos
    // Formato ISO 8601: "2024-01-15T10:30:00"
    // Generado automaticamente al crear el bombero
    val createdAt: String,

    // Timestamp de la ultima actualizacion del registro
    // Se actualiza automaticamente cada vez que se modifica el bombero
    // Formato ISO 8601: "2024-01-15T10:30:00"
    val updatedAt: String
) {
    // Propiedad computada: nombre completo del bombero
    // Se calcula dinamicamente combinando nombres y apellidos
    // Usado en la interfaz para mostrar el nombre completo sin concatenar manualmente
    // No se almacena en base de datos, se genera en tiempo de ejecucion
    val nombreCompleto: String
        get() = "$nombres $apellidos"

    // Propiedad computada: verifica si el bombero esta activo
    // Retorna true solo si el estado es exactamente "Activo"
    // Usado para filtros, validaciones y logica condicional en la UI
    // Ejemplo: solo bomberos activos pueden ser asignados a citaciones
    val isActivo: Boolean
        get() = estado == "Activo"
}
// Este modelo se conecta con:
// - BomberosScreen: lista todos los bomberos
// - DetalleScreen: muestra informacion completa de un bombero
// - FormScreen: crea o edita un bombero
// - BomberoCard: muestra un bombero en una tarjeta
// - BomberoRepository: maneja operaciones CRUD de bomberos
// - FakeDataSource: almacena los bomberos en memoria


