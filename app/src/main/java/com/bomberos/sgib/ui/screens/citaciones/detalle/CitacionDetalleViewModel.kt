package com.bomberos.sgib.ui.screens.citaciones.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bomberos.sgib.data.repository.CitacionRepository
import com.bomberos.sgib.domain.model.Citacion
import com.bomberos.sgib.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de citación
 */
@HiltViewModel
class CitacionDetalleViewModel @Inject constructor(
    private val citacionRepository: CitacionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val citacionId: Int = savedStateHandle.get<Int>("citacionId") ?: -1

    private val _citacionState = MutableStateFlow<Resource<Citacion>?>(null)
    val citacionState: StateFlow<Resource<Citacion>?> = _citacionState.asStateFlow()

    private val _confirmacionState = MutableStateFlow<Resource<Citacion>?>(null)
    val confirmacionState: StateFlow<Resource<Citacion>?> = _confirmacionState.asStateFlow()

    init {
        loadCitacion()
    }

    fun loadCitacion() {
        viewModelScope.launch {
            citacionRepository.getCitacionById(citacionId).collect { resource ->
                _citacionState.value = resource
            }
        }
    }

    fun confirmarAsistencia() {
        viewModelScope.launch {
            citacionRepository.confirmarAsistencia(citacionId).collect { resource ->
                _confirmacionState.value = resource
                if (resource is Resource.Success) {
                    // Recargar citación actualizada
                    loadCitacion()
                }
            }
        }
    }

    fun rechazarAsistencia() {
        viewModelScope.launch {
            citacionRepository.rechazarAsistencia(citacionId).collect { resource ->
                _confirmacionState.value = resource
                if (resource is Resource.Success) {
                    loadCitacion()
                }
            }
        }
    }

    fun clearConfirmacionState() {
        _confirmacionState.value = null
    }
}

