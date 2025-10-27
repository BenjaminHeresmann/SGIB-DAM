package com.bomberos.sgib.domain.model

// Modelo de dominio para Usuario autenticado
// Este modelo representa la informacion del usuario que ha iniciado sesion en la aplicacion
// Es parte de la capa de dominio (logica de negocio) y no depende de detalles de implementacion

// Data class: Clase especial de Kotlin que automaticamente genera:
// - equals() y hashCode(): para comparar objetos
// - toString(): para representacion en texto
// - copy(): para crear copias modificadas del objeto
// - componentN(): para desestructuracion de objetos
data class User(
    // ID unico del usuario en la base de datos
    // Tipo Int: numero entero usado como identificador primario
    val id: Int,

    // Email del usuario: usado para login y comunicaciones
    // Es unico en el sistema (un email por usuario)
    val email: String,

    // Nombre completo del usuario para mostrar en la interfaz
    val nombre: String,

    // Rol del usuario en el sistema: define permisos y responsabilidades
    // Ejemplos: "Comandante", "Capitan", "Teniente", "Bombero"
    val rol: String,

    // Tipo de usuario: define nivel de acceso al sistema
    // "admin": acceso completo, puede crear, editar y eliminar
    // "usuario": acceso limitado, solo puede ver informacion
    val tipo: String,

    // Estado de actividad del usuario
    // true: usuario activo, puede usar el sistema
    // false: usuario inactivo, bloqueado o dado de baja
    val activo: Boolean
)
// Este modelo se usa en:
// - LoginScreen: despues de autenticar, muestra el nombre del usuario
// - DashboardScreen: determina que acciones puede realizar el usuario
// - AuthRepository: para almacenar los datos del usuario logueado
// - PreferencesManager: para persistir la sesion del usuario


