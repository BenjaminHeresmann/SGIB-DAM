package com.bomberos.sgib.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bomberos.sgib.data.repository.AuthRepository
import com.bomberos.sgib.data.repository.BomberoRepository
import com.bomberos.sgib.domain.model.Stats
import com.bomberos.sgib.domain.model.User
import com.bomberos.sgib.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para el Dashboard
 * Maneja estadísticas y datos del usuario
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bomberoRepository: BomberoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // Usuario actual
    val currentUser: StateFlow<User?> = authRepository.getCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Estado de las estadísticas
    private val _statsState = MutableStateFlow<Resource<Stats>?>(null)
    val statsState: StateFlow<Resource<Stats>?> = _statsState.asStateFlow()

    init {
        loadStats()
    }

    /**
     * Cargar estadísticas
     */
    fun loadStats() {
        viewModelScope.launch {
            bomberoRepository.getStats()
                .collect { resource ->
                    _statsState.value = resource
                }
        }
    }

    /**
     * Refrescar datos
     */
    fun refresh() {
        loadStats()
    }

    /**
     * Logout
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

