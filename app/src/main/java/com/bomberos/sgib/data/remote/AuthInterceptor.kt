package com.bomberos.sgib.data.remote

import com.bomberos.sgib.data.local.PreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor que agrega automáticamente el token JWT a las peticiones
 */
class AuthInterceptor(
    private val preferencesManager: PreferencesManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Obtener token del DataStore (bloqueante para interceptor)
        val token = runBlocking {
            preferencesManager.getToken().first()
        }

        // Si hay token, agregarlo al header Authorization
        val newRequest = if (!token.isNullOrEmpty()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(newRequest)
    }
}

