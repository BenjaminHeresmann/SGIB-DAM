package com.bomberos.sgib.ui.screens.citaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bomberos.sgib.data.repository.CitacionRepository
import com.bomberos.sgib.domain.model.Citacion
import com.bomberos.sgib.domain.model.EstadoCitacion
import com.bomberos.sgib.domain.model.TipoActividad
import com.bomberos.sgib.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de lista de citaciones
 */
@HiltViewModel
class CitacionesViewModel @Inject constructor(
    private val citacionRepository: CitacionRepository
) : ViewModel() {

    private val _citacionesState = MutableStateFlow<Resource<List<Citacion>>?>(null)
    val citacionesState: StateFlow<Resource<List<Citacion>>?> = _citacionesState.asStateFlow()

    private val _filtroEstado = MutableStateFlow<EstadoCitacion?>(null)
    val filtroEstado: StateFlow<EstadoCitacion?> = _filtroEstado.asStateFlow()

    private val _filtroTipoActividad = MutableStateFlow<TipoActividad?>(null)
    val filtroTipoActividad: StateFlow<TipoActividad?> = _filtroTipoActividad.asStateFlow()

    init {
        loadCitaciones()
    }

    fun loadCitaciones() {
        viewModelScope.launch {
            citacionRepository.getCitaciones(
                estado = _filtroEstado.value?.name,
                tipoActividad = _filtroTipoActividad.value?.name
            ).collect { resource ->
                _citacionesState.value = resource
            }
        }
    }

    fun setFiltroEstado(estado: EstadoCitacion?) {
        _filtroEstado.value = estado
        loadCitaciones()
    }

    fun setFiltroTipoActividad(tipo: TipoActividad?) {
        _filtroTipoActividad.value = tipo
        loadCitaciones()
    }

    fun limpiarFiltros() {
        _filtroEstado.value = null
        _filtroTipoActividad.value = null
        loadCitaciones()
    }

    fun confirmarAsistencia(citacionId: Int) {
        viewModelScope.launch {
            citacionRepository.confirmarAsistencia(citacionId).collect { resource ->
                if (resource is Resource.Success) {
                    loadCitaciones() // Recargar lista después de confirmar
                }
            }
        }
    }

    fun rechazarAsistencia(citacionId: Int) {
        viewModelScope.launch {
            citacionRepository.rechazarAsistencia(citacionId).collect { resource ->
                if (resource is Resource.Success) {
                    loadCitaciones() // Recargar lista después de rechazar
                }
            }
        }
    }
}

