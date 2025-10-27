package com.bomberos.sgib.ui.screens.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bomberos.sgib.data.repository.BomberoRepositoryLocal
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
    private val repository: BomberoRepositoryLocal,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(FormState())
    val state: StateFlow<FormState> = _state.asStateFlow()

    private val bomberoId: Int? = try {
        savedStateHandle.get<Int>("bomberoId")?.takeIf { it > 0 }
    } catch (_: Exception) {
        null
    }

    init {
        // Si hay ID válido (mayor a 0), estamos editando
        bomberoId?.let { id ->
            loadBombero(id)
        }
    }

    /**
     * Cargar datos del bombero para editar
     */
    private fun loadBombero(id: Int) {
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
                FormField.TELEFONO -> currentState.copy(telefono = value)
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
                // Crear objeto Bombero con los datos del formulario
                val bombero = com.bomberos.sgib.domain.model.Bombero(
                    id = bomberoId ?: 0,
                    nombres = _state.value.nombres.trim(),
                    apellidos = _state.value.apellidos.trim(),
                    rango = _state.value.rango,
                    especialidad = _state.value.especialidad.trim().ifBlank { null },
                    estado = _state.value.estado,
                    telefono = _state.value.telefono.trim().ifBlank { null },
                    email = _state.value.email.trim().ifBlank { null },
                    direccion = _state.value.direccion.trim().ifBlank { null },
                    fechaIngreso = java.time.LocalDate.now().toString(),
                    fotoUrl = _state.value.fotoUri,
                    createdAt = "",
                    updatedAt = ""
                )

                // Decidir si crear o actualizar
                val flow = if (bomberoId != null && bomberoId!! > 0) {
                    repository.updateBombero(bombero)
                } else {
                    repository.createBombero(bombero)
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

