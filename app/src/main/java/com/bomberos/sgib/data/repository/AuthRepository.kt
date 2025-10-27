package com.bomberos.sgib.data.repository

import com.bomberos.sgib.data.local.PreferencesManager
import com.bomberos.sgib.data.remote.ApiService
import com.bomberos.sgib.data.remote.dto.LoginRequest
import com.bomberos.sgib.domain.model.User
import com.bomberos.sgib.util.toUser
import com.bomberos.sgib.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Repositorio para autenticación
 * Maneja login, logout y sesión del usuario
 */
class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val preferencesManager: PreferencesManager
) {

    /**
     * Login con email y password
     */
    fun login(email: String, password: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val response = api.login(LoginRequest(email, password))

            if (response.isSuccessful && response.body()?.success == true) {
                val loginData = response.body()?.data

                if (loginData != null) {
                    val user = loginData.user.toUser()
                    val token = loginData.token

                    // Guardar en DataStore
                    preferencesManager.saveToken(token)
                    preferencesManager.saveUser(user)

                    emit(Resource.Success(user))
                } else {
                    emit(Resource.Error("Error en los datos de respuesta"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Credenciales inválidas"
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
     * Logout
     */
    suspend fun logout() {
        try {
            api.logout()
        } catch (e: Exception) {
            // Ignorar errores de logout en servidor
        } finally {
            // Siempre limpiar datos locales
            preferencesManager.clearAll()
        }
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

