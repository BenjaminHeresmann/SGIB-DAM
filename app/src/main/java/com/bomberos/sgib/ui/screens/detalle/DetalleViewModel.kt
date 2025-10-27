package com.bomberos.sgib.ui.screens.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bomberos.sgib.data.repository.BomberoRepositoryLocal
import com.bomberos.sgib.domain.model.Bombero
import com.bomberos.sgib.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de bombero
 */
@HiltViewModel
class DetalleViewModel @Inject constructor(
    private val repository: BomberoRepositoryLocal,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetalleState())
    val state: StateFlow<DetalleState> = _state.asStateFlow()

    init {
        // Obtener ID del bombero desde los argumentos de navegación
        val bomberoId = savedStateHandle.get<Int>("bomberoId")
        if (bomberoId != null && bomberoId > 0) {
            loadBombero(bomberoId)
        } else {
            _state.update { it.copy(error = "ID de bombero inválido") }
        }
    }

    /**
     * Cargar datos del bombero
     */
    private fun loadBombero(id: Int) {
        repository.getBomberoById(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true, error = null) }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            bombero = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error desconocido"
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Recargar datos
     */
    fun reload() {
        _state.value.bombero?.let { loadBombero(it.id) }
    }

    /**
     * Eliminar bombero
     */
    fun deleteBombero(onSuccess: () -> Unit) {
        val bomberoId = _state.value.bombero?.id ?: return

        repository.deleteBombero(bomberoId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true, error = null) }
                }
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error al eliminar"
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

/**
 * Estado de la pantalla de detalle
 */
data class DetalleState(
    val bombero: Bombero? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

