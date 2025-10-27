package com.bomberos.sgib.util

import android.util.Patterns
import java.util.regex.Pattern

// Objeto Validators: Coleccion de funciones de validacion para formularios
// Un objeto en Kotlin es un singleton: solo existe una instancia en toda la aplicacion
// Agrupa funciones de validacion reutilizables que se usan en toda la app
// Permite validar datos de entrada del usuario antes de procesarlos o enviarlos

object Validators {

    // Funcion isValidEmail: Valida que un string tenga formato de email valido
    // Parametro email: El string a validar
    // Retorna: true si es un email valido, false si no
    fun isValidEmail(email: String): Boolean {
        // Primero verifica que el email no este vacio (isNotBlank)
        // Luego usa Patterns.EMAIL_ADDRESS (patron de Android) para validar formato
        // Patterns.EMAIL_ADDRESS valida estructura: nombre@dominio.extension
        // Ejemplo valido: "juan@bomberos.cl"
        // Ejemplo invalido: "juan@", "@bomberos", "juan.bomberos.cl"
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    // Se conecta con:
    // - LoginScreen: valida email antes de intentar login
    // - FormScreen: valida email al crear/editar bombero
    // - ValidatedTextField: muestra error si el email es invalido

    // Funcion isNotEmpty: Valida que un string no este vacio
    // Parametro text: El string a validar
    // Retorna: true si tiene contenido, false si esta vacio o solo tiene espacios
    fun isNotEmpty(text: String): Boolean {
        // trim() elimina espacios al inicio y final
        // isEmpty() verifica si el string resultante esta vacio
        // Ejemplo valido: "Juan", "  Pedro  " (se convierte en "Pedro")
        // Ejemplo invalido: "", "   " (solo espacios)
        return text.trim().isNotEmpty()
    }
    // Se conecta con:
    // - FormScreen: valida que campos obligatorios no esten vacios
    // - ValidatedTextField: muestra error si el campo esta vacio

    // Funcion hasMinLength: Valida que un string tenga longitud minima
    // Parametro text: El string a validar
    // Parametro minLength: Longitud minima requerida
    // Retorna: true si cumple la longitud minima, false si no
    fun hasMinLength(text: String, minLength: Int): Boolean {
        // Compara la longitud del string con la longitud minima requerida
        // Ejemplo: hasMinLength("hola", 3) retorna true
        // Ejemplo: hasMinLength("hi", 3) retorna false
        return text.length >= minLength
    }
    // Se conecta con:
    // - LoginScreen: valida que la contrasena tenga minimo 4 caracteres
    // - ValidatedTextField: validacion personalizable de longitud

    // Funcion isValidName: Valida que un nombre tenga al menos 2 caracteres
    // Parametro name: El nombre a validar
    // Retorna: true si tiene 2 o mas caracteres (sin contar espacios), false si no
    fun isValidName(name: String): Boolean {
        // trim() elimina espacios al inicio y final
        // length verifica que tenga al menos 2 caracteres
        // Ejemplo valido: "Juan", "María Paz", "  Pedro  "
        // Ejemplo invalido: "J", " ", ""
        return name.trim().length >= 2
    }
    // Se conecta con:
    // - FormScreen: valida nombres y apellidos de bomberos
    // - ValidatedTextField: muestra error si el nombre es muy corto

    // Funcion isValidPhone: Valida formato de numero telefonico
    // Parametro phone: El numero de telefono a validar
    // Retorna: true si es valido o esta vacio (es opcional), false si formato es invalido
    fun isValidPhone(phone: String): Boolean {
        // Si esta en blanco, retorna true porque el telefono es opcional
        if (phone.isBlank()) return true

        // Pattern regex para validar formato de telefono
        // ^: inicio del string
        // \\+?: signo + opcional (para codigo de pais)
        // [\\d\\s\\-\\(\\)]: digitos, espacios, guiones, parentesis
        // {8,20}: debe tener entre 8 y 20 caracteres
        // $: fin del string
        // Ejemplos validos: "912345678", "+56 9 1234 5678", "(+56) 9-1234-5678"
        // Ejemplos invalidos: "123", "abc123", "12345678901234567890123"
        val pattern = Pattern.compile("^\\+?[\\d\\s\\-\\(\\)]{8,20}$")
        return pattern.matcher(phone).matches()
    }
    // Se conecta con:
    // - FormScreen: valida telefono al crear/editar bombero
    // - ValidatedTextField: muestra error si el formato de telefono es invalido

    // Funcion isValidPassword: Valida que la contrasena tenga longitud minima
    // Parametro password: La contrasena a validar
    // Retorna: true si tiene 4 o mas caracteres, false si no
    fun isValidPassword(password: String): Boolean {
        // Valida longitud minima de 4 caracteres para la contrasena
        // En produccion, normalmente se requieren contrasenas mas seguras
        // (mayusculas, minusculas, numeros, caracteres especiales, minimo 8 caracteres)
        // Ejemplo valido: "1234", "pass123", "MiContrasena"
        // Ejemplo invalido: "123", "ab"
        return password.length >= 4
    }
    // Se conecta con:
    // - LoginScreen: valida contrasena antes de intentar login
    // - ValidatedTextField: muestra error si la contrasena es muy corta
}
// Este objeto se usa en toda la aplicacion para validar entradas de usuario
// Centraliza la logica de validacion para mantener consistencia
// Facilita el mantenimiento: cambiar una validacion aqui afecta toda la app


