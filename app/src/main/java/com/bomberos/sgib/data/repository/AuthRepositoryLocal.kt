package com.bomberos.sgib.data.repository

import com.bomberos.sgib.data.local.PreferencesManager
import com.bomberos.sgib.data.local.FakeDataSource
import com.bomberos.sgib.domain.model.User
import com.bomberos.sgib.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Repositorio para autenticación
 * VERSIÓN SIN BACKEND - Usa datos locales simulados
 */
class AuthRepositoryLocal @Inject constructor(
    private val preferencesManager: PreferencesManager
) {

    /**
     * Login con email y password (simulado - sin backend)
     */
    fun login(email: String, password: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            // Simular delay de red
            delay(1000)

            // Validar credenciales con datos locales
            val user = FakeDataSource.validateCredentials(email, password)

            if (user != null) {
                // Guardar usuario y token simulado
                val fakeToken = "fake_token_${user.id}_${System.currentTimeMillis()}"
                preferencesManager.saveToken(fakeToken)
                preferencesManager.saveUser(user)

                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }

    /**
     * Logout
     */
    suspend fun logout() {
        // Limpiar datos locales
        preferencesManager.clearAll()
    }

    /**
     * Verificar si el usuario está logueado
     */
    fun isLoggedIn(): Flow<Boolean> {
        return preferencesManager.isLoggedIn()
    }

    /**
     * Obtener usuario actual
     */
    fun getCurrentUser(): Flow<User?> {
        return preferencesManager.getUser()
    }

    /**
     * Habilitar/deshabilitar biométrica
     */
    suspend fun setBiometricEnabled(enabled: Boolean) {
        preferencesManager.setBiometricEnabled(enabled)
    }

    /**
     * Verificar si biométrica está habilitada
     */
    fun isBiometricEnabled(): Flow<Boolean> {
        return preferencesManager.isBiometricEnabled()
    }
}

