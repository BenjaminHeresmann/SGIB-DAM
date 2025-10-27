package com.bomberos.sgib.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bomberos.sgib.domain.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Clase PreferencesManager: Gestiona la persistencia de datos con DataStore
// DataStore es el reemplazo moderno de SharedPreferences en Android
// Proporciona almacenamiento asincrono y type-safe de preferencias

// PROPOSITO:
// - Guardar token de autenticacion (JWT)
// - Guardar datos del usuario logueado
// - Guardar preferencias de la app (biometria habilitada)
// - Guardar credenciales para login biometrico
// - Mantener el estado de sesion del usuario

// VENTAJAS DE DATASTORE vs SHAREDPREFERENCES:
// - Completamente asincrono (usa Kotlin Coroutines)
// - Type-safe (no puede guardar String donde espera Int)
// - Maneja errores de lectura/escritura de forma robusta
// - Soporta transacciones atomicas
// - Observable con Flow (cambios en tiempo real)

// Constructor: recibe Context para acceder al DataStore
// Context es inyectado automaticamente por Hilt desde AppModule
class PreferencesManager(private val context: Context) {

    // Extension property: crea una instancia de DataStore asociada al Context
    // preferencesDataStore: delegado que crea el DataStore la primera vez que se accede
    // name: nombre del archivo donde se guardan las preferencias
    // El archivo se almacena en: /data/data/com.bomberos.sgib/files/datastore/bomberos_prefs
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bomberos_prefs")

    // Gson: Libreria para convertir objetos a JSON y viceversa
    // Se usa para serializar el objeto User y guardarlo como String
    private val gson = Gson()

    // Companion Object: Define constantes que son compartidas por todas las instancias
    // Similar a static en Java
    companion object {
        // Claves para acceder a los valores en DataStore
        // PreferencesKey: tipo seguro que define el tipo de dato almacenado

        // Clave para el token JWT de autenticacion
        // Tipo String: el token es una cadena de texto
        val TOKEN_KEY = stringPreferencesKey("jwt_token")

        // Clave para los datos completos del usuario en formato JSON
        // Se serializa el objeto User a JSON y se guarda como String
        val USER_KEY = stringPreferencesKey("user_data")

        // Clave para indicar si hay una sesion activa
        // Tipo Boolean: true si esta logueado, false si no
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

        // Clave para indicar si la autenticacion biometrica esta habilitada
        // Tipo Boolean: true si el usuario activo la biometria
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")

        // Clave para guardar el ID del usuario logueado
        // Tipo Int: identificador numerico del usuario
        val USER_ID = intPreferencesKey("user_id")

        // Clave para guardar el email del usuario logueado
        // Usado para mostrar en la UI sin deserializar todo el User
        val USER_EMAIL = stringPreferencesKey("user_email")

        // Clave para guardar el email usado en login biometrico
        // Se guarda codificado en Base64 (no es seguro para produccion)
        val BIOMETRIC_EMAIL = stringPreferencesKey("biometric_email")

        // Clave para guardar la contrasena usada en login biometrico
        // Se guarda codificada en Base64 (no es seguro para produccion)
        // En produccion DEBE usarse Android Keystore o EncryptedSharedPreferences
        val BIOMETRIC_PASSWORD = stringPreferencesKey("biometric_password")
    }

    // SECCION: GESTION DE TOKEN
    // El token JWT se usa para autenticar peticiones al backend
    // Actualmente no se usa porque trabajamos sin backend

    // Funcion saveToken: Guarda el token de autenticacion
    // Parametro token: El token JWT recibido del backend
    // Es suspend porque la operacion es asincrona
    suspend fun saveToken(token: String) {
        // edit: Abre una transaccion para modificar preferencias
        // La transaccion es atomica: se aplican todos los cambios o ninguno
        context.dataStore.edit { preferences ->
            // Guarda el token usando la clave TOKEN_KEY
            preferences[TOKEN_KEY] = token
        }
    }
    // Se conecta con: AuthRepository al hacer login exitoso

    // Funcion getToken: Obtiene el token guardado
    // Retorna: Flow<String?> que emite el token actual y cambios futuros
    // Flow es un stream observable que puede emitir multiples valores
    fun getToken(): Flow<String?> {
        // data: Flow que emite Preferences cada vez que cambian
        return context.dataStore.data.map { preferences ->
            // Obtiene el valor asociado a TOKEN_KEY, o null si no existe
            preferences[TOKEN_KEY]
        }
    }
    // Se conecta con: AuthInterceptor para agregar token a peticiones HTTP

    // Funcion clearToken: Elimina el token guardado
    // Se usa al hacer logout para invalidar la sesion
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            // remove: Elimina la clave y su valor asociado
            preferences.remove(TOKEN_KEY)
        }
    }
    // Se conecta con: AuthRepository al hacer logout

    // ==================== USER DATA ====================

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = gson.toJson(user)
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = user.id
            preferences[USER_EMAIL] = user.email
        }
    }

    fun getUser(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_KEY]?.let { json ->
                try {
                    gson.fromJson(json, User::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    }

    // ==================== BIOMETRIC ====================

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED] = enabled
        }
    }

    fun isBiometricEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[BIOMETRIC_ENABLED] ?: false
        }
    }

    /**
     * Guardar credenciales para login biométrico
     * NOTA: En producción, usar EncryptedSharedPreferences o Android Keystore
     */
    suspend fun saveBiometricCredentials(email: String, password: String) {
        context.dataStore.edit { preferences ->
            // En este ejemplo educativo usamos base64 simple
            // En producción DEBE usarse encriptación real
            val encodedEmail = android.util.Base64.encodeToString(
                email.toByteArray(),
                android.util.Base64.DEFAULT
            )
            val encodedPassword = android.util.Base64.encodeToString(
                password.toByteArray(),
                android.util.Base64.DEFAULT
            )
            preferences[BIOMETRIC_EMAIL] = encodedEmail
            preferences[BIOMETRIC_PASSWORD] = encodedPassword
        }
    }

    /**
     * Obtener credenciales guardadas para biometría
     */
    fun getBiometricCredentials(): Flow<Pair<String, String>?> {
        return context.dataStore.data.map { preferences ->
            val encodedEmail = preferences[BIOMETRIC_EMAIL]
            val encodedPassword = preferences[BIOMETRIC_PASSWORD]

            if (encodedEmail != null && encodedPassword != null) {
                try {
                    val email = String(
                        android.util.Base64.decode(encodedEmail, android.util.Base64.DEFAULT)
                    )
                    val password = String(
                        android.util.Base64.decode(encodedPassword, android.util.Base64.DEFAULT)
                    )
                    Pair(email, password)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }

    /**
     * Limpiar credenciales biométricas
     */
    suspend fun clearBiometricCredentials() {
        context.dataStore.edit { preferences ->
            preferences.remove(BIOMETRIC_EMAIL)
            preferences.remove(BIOMETRIC_PASSWORD)
        }
    }

    // ==================== LOGOUT ====================

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

