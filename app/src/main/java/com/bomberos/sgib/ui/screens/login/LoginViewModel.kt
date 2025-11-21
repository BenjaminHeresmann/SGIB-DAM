package com.bomberos.sgib.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bomberos.sgib.data.repository.AuthRepository
import com.bomberos.sgib.domain.model.User
import com.bomberos.sgib.util.Resource
import com.bomberos.sgib.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de Login
 * Maneja la lógica de autenticación y validaciones
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Estado del formulario
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible.asStateFlow()

    // Errores de validación
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    // Estado de la petición
    private val _loginState = MutableStateFlow<Resource<User>?>(null)
    val loginState: StateFlow<Resource<User>?> = _loginState.asStateFlow()

    // Estado de biometría
    val isBiometricEnabled = authRepository.isBiometricEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    /**
     * Actualizar email
     */
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        // Limpiar error al escribir
        if (_emailError.value != null) {
            _emailError.value = null
        }
    }

    /**
     * Actualizar password
     */
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        // Limpiar error al escribir
        if (_passwordError.value != null) {
            _passwordError.value = null
        }
    }

    /**
     * Toggle visibilidad de password
     */
    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    /**
     * Validar formulario
     */
    private fun validateForm(): Boolean {
        var isValid = true

        // Validar email
        if (_email.value.isBlank()) {
            _emailError.value = "El email es requerido"
            isValid = false
        }

        // Validar password
        if (_password.value.isBlank()) {
            _passwordError.value = "La contraseña es requerida"
            isValid = false
        } else if (!Validators.isValidPassword(_password.value)) {
            _passwordError.value = "La contraseña debe tener al menos 4 caracteres"
            isValid = false
        }

        return isValid
    }

    /**
     * Intentar login
     */
    fun login() {
        // Limpiar errores anteriores
        _emailError.value = null
        _passwordError.value = null

        // Validar formulario
        if (!validateForm()) {
            return
        }

        // Ejecutar login
        viewModelScope.launch {
            authRepository.login(_email.value, _password.value)
                .collect { resource ->
                    _loginState.value = resource
                }
        }
    }

    /**
     * Login con biometría
     * TODO: Implementar con backend
     */
    fun loginWithBiometric() {
        viewModelScope.launch {
            // Por ahora no implementado - requiere backend support
            _loginState.value = Resource.Error("Biometría no disponible aún")
        }
    }

    /**
     * Habilitar login biométrico (guardar credenciales)
     * TODO: Implementar con backend
     */
    fun enableBiometricLogin() {
        // Por ahora no implementado - requiere backend support
    }

    /**
     * Rellenar credenciales de prueba (admin)
     */
    fun fillAdminCredentials() {
        _email.value = "admin"
        _password.value = "1234"
    }

    /**
     * Rellenar credenciales de prueba (usuario)
     */
    fun fillUserCredentials() {
        _email.value = "bombero@bomberos.cl"
        _password.value = "bomb345"
    }

    /**
     * Limpiar estado de login
     */
    fun clearLoginState() {
        _loginState.value = null
    }
}

