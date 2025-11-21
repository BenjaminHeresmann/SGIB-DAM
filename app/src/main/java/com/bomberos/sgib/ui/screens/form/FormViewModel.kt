package com.bomberos.sgib.ui.screens.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bomberos.sgib.data.repository.BomberoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para el formulario de bombero (crear/editar)
 */
@HiltViewModel
class FormViewModel @Inject constructor(
    private val repository: BomberoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(FormState())
    val state: StateFlow<FormState> = _state.asStateFlow()

    private val bomberoId: String? = try {
        savedStateHandle.get<String>("bomberoId")?.takeIf { it.isNotBlank() }
    } catch (_: Exception) {
        null
    }

    init {
        // Si hay ID válido (not blank), estamos editando
        bomberoId?.let { id ->
            loadBombero(id)
        }
    }

    /**
     * Cargar datos del bombero para editar
     */
    private fun loadBombero(id: String) {
        viewModelScope.launch {
            repository.getBomberoById(id).collect { result ->
                when (result) {
                    is com.bomberos.sgib.util.Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is com.bomberos.sgib.util.Resource.Success -> {
                        result.data?.let { bombero ->
                            _state.update {
                                it.copy(
                                    isEditMode = true,
                                    nombres = bombero.nombres,
                                    apellidos = bombero.apellidos,
                                    rango = bombero.rango,
                                    especialidad = bombero.especialidad ?: "",
                                    estado = bombero.estado,
                                    telefono = bombero.telefono ?: "",
                                    email = bombero.email ?: "",
                                    direccion = bombero.direccion ?: "",
                                    fotoUri = bombero.fotoUrl,
                                    isLoading = false
                                )
                            }
                        }
                    }
                    is com.bomberos.sgib.util.Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Error al cargar bombero"
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Actualizar campo del formulario
     */
    fun updateField(field: FormField, value: String) {
        _state.update { currentState ->
            when (field) {
                FormField.NOMBRES -> currentState.copy(nombres = value)
                FormField.APELLIDOS -> currentState.copy(apellidos = value)
                FormField.RANGO -> currentState.copy(rango = value)
                FormField.ESPECIALIDAD -> currentState.copy(especialidad = value)
                FormField.ESTADO -> currentState.copy(estado = value)
                FormField.TELEFONO -> {
                    // Solo permitir números, espacios, +, paréntesis y guiones
                    val filteredValue = value.filter {
                        it.isDigit() || it == ' ' || it == '+' || it == '-' || it == '(' || it == ')'
                    }
                    currentState.copy(telefono = filteredValue)
                }
                FormField.EMAIL -> currentState.copy(email = value)
                FormField.DIRECCION -> currentState.copy(direccion = value)
            }
        }
    }

    /**
     * Actualizar foto
     */
    fun updateFoto(fotoUri: String?) {
        _state.update { it.copy(fotoUri = fotoUri) }
    }

    /**
     * Validar formulario
     */
    private fun validateForm(): Boolean {
        val errors = mutableMapOf<FormField, String>()

        if (_state.value.nombres.isBlank()) {
            errors[FormField.NOMBRES] = "Los nombres son requeridos"
        }
        if (_state.value.apellidos.isBlank()) {
            errors[FormField.APELLIDOS] = "Los apellidos son requeridos"
        }
        if (_state.value.rango.isBlank()) {
            errors[FormField.RANGO] = "El rango es requerido"
        }
        if (_state.value.estado.isBlank()) {
            errors[FormField.ESTADO] = "El estado es requerido"
        }

        // Validar email si no está vacío
        if (_state.value.email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(_state.value.email).matches()) {
            errors[FormField.EMAIL] = "Email inválido"
        }

        // Validar teléfono si no está vacío
        if (_state.value.telefono.isNotBlank()) {
            // Extraer solo los dígitos para validar longitud
            val digitsOnly = _state.value.telefono.filter { it.isDigit() }
            if (digitsOnly.length < 8) {
                errors[FormField.TELEFONO] = "El teléfono debe tener al menos 8 dígitos"
            } else if (digitsOnly.length > 15) {
                errors[FormField.TELEFONO] = "El teléfono no puede tener más de 15 dígitos"
            }
        }

        _state.update { it.copy(errors = errors) }
        return errors.isEmpty()
    }

    /**
     * Guardar bombero
     */
    fun guardar(onSuccess: () -> Unit) {
        if (!validateForm()) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Crear BomberoRequest con los datos del formulario
                val request = com.bomberos.sgib.data.remote.dto.BomberoRequest(
                    nombres = _state.value.nombres.trim(),
                    apellidos = _state.value.apellidos.trim(),
                    rango = _state.value.rango,
                    especialidad = _state.value.especialidad.trim().ifBlank { null },
                    estado = _state.value.estado,
                    telefono = _state.value.telefono.trim().ifBlank { null },
                    email = _state.value.email.trim().ifBlank { null },
                    direccion = _state.value.direccion.trim().ifBlank { null },
                    fechaIngreso = java.time.LocalDate.now().toString(),
                    fotoUrl = _state.value.fotoUri
                )

                // Decidir si crear o actualizar
                val flow = if (bomberoId != null && bomberoId!!.isNotBlank()) {
                    repository.updateBombero(bomberoId!!, request)
                } else {
                    repository.createBombero(request)
                }

                // Procesar resultado
                flow.collect { result ->
                    when (result) {
                        is com.bomberos.sgib.util.Resource.Loading -> {
                            // Mantener isLoading = true
                        }
                        is com.bomberos.sgib.util.Resource.Success -> {
                            _state.update { it.copy(isLoading = false, guardadoExitoso = true) }
                            onSuccess()
                        }
                        is com.bomberos.sgib.util.Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.message ?: "Error al guardar"
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al guardar: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    /**
     * Limpiar error
     */
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

/**
 * Estado del formulario
 */
data class FormState(
    val isEditMode: Boolean = false,
    val nombres: String = "",
    val apellidos: String = "",
    val rango: String = "Bombero",
    val especialidad: String = "",
    val estado: String = "Activo",
    val telefono: String = "",
    val email: String = "",
    val direccion: String = "",
    val fotoUri: String? = null,
    val errors: Map<FormField, String> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val guardadoExitoso: Boolean = false
)

/**
 * Campos del formulario
 */
enum class FormField {
    NOMBRES,
    APELLIDOS,
    RANGO,
    ESPECIALIDAD,
    ESTADO,
    TELEFONO,
    EMAIL,
    DIRECCION
}

