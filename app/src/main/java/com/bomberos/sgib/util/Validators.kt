package com.bomberos.sgib.util

import android.util.Patterns
import java.util.regex.Pattern

/**
 * Validadores para formularios
 */
object Validators {

    /**
     * Valida email
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Valida que no esté vacío
     */
    fun isNotEmpty(text: String): Boolean {
        return text.trim().isNotEmpty()
    }

    /**
     * Valida longitud mínima
     */
    fun hasMinLength(text: String, minLength: Int): Boolean {
        return text.length >= minLength
    }

    /**
     * Valida nombres (al menos 2 caracteres)
     */
    fun isValidName(name: String): Boolean {
        return name.trim().length >= 2
    }

    /**
     * Valida teléfono chileno
     */
    fun isValidPhone(phone: String): Boolean {
        if (phone.isBlank()) return true // Opcional
        val pattern = Pattern.compile("^\\+?[\\d\\s\\-\\(\\)]{8,20}$")
        return pattern.matcher(phone).matches()
    }

    /**
     * Valida contraseña (mínimo 4 caracteres)
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= 4
    }
}

