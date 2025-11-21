package com.bomberos.sgib.data.remote

import com.bomberos.sgib.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service - Define todos los endpoints del backend
 */
interface ApiService {

    // ==================== AUTH ====================

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    // ==================== BOMBEROS ====================

    @GET("bomberos")
    suspend fun getBomberos(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("search") search: String = "",
        @Query("rango") rango: String = "",
        @Query("estado") estado: String = "Activo",
        @Query("sortBy") sortBy: String = "apellidos",
        @Query("sortOrder") sortOrder: String = "asc"
    ): Response<BomberosResponse>

    @GET("bomberos/{id}")
    suspend fun getBomberoById(@Path("id") id: String): Response<BomberoResponse>

    @POST("bomberos")
    suspend fun createBombero(@Body request: BomberoRequest): Response<BomberoResponse>

    @PUT("bomberos/{id}")
    suspend fun updateBombero(
        @Path("id") id: String,
        @Body request: BomberoRequest
    ): Response<BomberoResponse>

    @DELETE("bomberos/{id}")
    suspend fun deleteBombero(@Path("id") id: String): Response<BomberoResponse>

    @GET("bomberos/stats/general")
    suspend fun getStats(): Response<StatsResponse>

    // ==================== CITACIONES ====================

    @GET("citaciones")
    suspend fun getCitaciones(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("estado") estado: String? = null,
        @Query("tipoActividad") tipoActividad: String? = null,
        @Query("sortBy") sortBy: String = "fecha",
        @Query("sortOrder") sortOrder: String = "desc"
    ): Response<CitacionListResponse>

    @GET("citaciones/{id}")
    suspend fun getCitacionById(@Path("id") id: Int): Response<CitacionResponse>

    @POST("citaciones")
    suspend fun createCitacion(@Body request: CitacionCreateRequest): Response<CitacionResponse>

    @PUT("citaciones/{id}")
    suspend fun updateCitacion(
        @Path("id") id: Int,
        @Body request: CitacionUpdateRequest
    ): Response<CitacionResponse>

    @DELETE("citaciones/{id}")
    suspend fun deleteCitacion(@Path("id") id: Int): Response<CitacionResponse>

    @POST("citaciones/{id}/confirmar")
    suspend fun confirmarAsistencia(@Path("id") id: Int): Response<CitacionResponse>

    @POST("citaciones/{id}/rechazar")
    suspend fun rechazarAsistencia(@Path("id") id: Int): Response<CitacionResponse>
}

