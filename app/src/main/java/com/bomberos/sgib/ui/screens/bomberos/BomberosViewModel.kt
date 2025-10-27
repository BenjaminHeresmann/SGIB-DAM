package com.bomberos.sgib.ui.screens.bomberos

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
 * ViewModel para la pantalla de lista de bomberos
 */
@HiltViewModel
class BomberosViewModel @Inject constructor(
    private val repository: BomberoRepositoryLocal
) : ViewModel() {

    private val _state = MutableStateFlow(BomberosState())
    val state: StateFlow<BomberosState> = _state.asStateFlow()

    init {
        loadBomberos()
    }

    /**
     * Cargar lista de bomberos
     */
    fun loadBomberos() {
        repository.getBomberos(
            search = _state.value.searchQuery,
            estado = _state.value.filtroEstado
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true, error = null) }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            bomberos = result.data ?: emptyList(),
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
     * Actualizar búsqueda
     */
    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        loadBomberos()
    }

    /**
     * Cambiar filtro de estado
     */
    fun onFiltroEstadoChange(estado: String) {
        _state.update { it.copy(filtroEstado = estado) }
        loadBomberos()
    }

    /**
     * Refrescar datos (pull-to-refresh)
     */
    fun refresh() {
        _state.update { it.copy(isRefreshing = true) }
        repository.getBomberos(
            search = _state.value.searchQuery,
            estado = _state.value.filtroEstado
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    // Mantener isRefreshing activo
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            bomberos = result.data ?: emptyList(),
                            isRefreshing = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            error = result.message ?: "Error desconocido"
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Limpiar error
     */
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

/**
 * Estado de la pantalla de bomberos
 */
data class BomberosState(
    val bomberos: List<Bombero> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filtroEstado: String = "Activo"
)

