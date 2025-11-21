package com.bomberos.sgib.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para request de login
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * DTO para response de login
 */
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val user: UserDto,
    val token: String
)

data class UserDto(
    @SerializedName("_id")
    val id: String,
    val email: String,
    val nombre: String,
    val rol: String,
    val tipo: String,
    val activo: Boolean
)

