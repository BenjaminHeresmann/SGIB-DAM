package com.bomberos.sgib.ui.screens.citaciones

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bomberos.sgib.domain.model.Citacion
import com.bomberos.sgib.domain.model.EstadoCitacion
import com.bomberos.sgib.domain.model.TipoActividad
import com.bomberos.sgib.util.Resource
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Pantalla de lista de citaciones
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitacionesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetalle: (Int) -> Unit,
    viewModel: CitacionesViewModel = hiltViewModel()
) {
    val citacionesState by viewModel.citacionesState.collectAsState()
    val filtroEstado by viewModel.filtroEstado.collectAsState()
    val filtroTipoActividad by viewModel.filtroTipoActividad.collectAsState()

    var showFiltros by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Citaciones") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showFiltros = !showFiltros }) {
                        Icon(
                            if (filtroEstado != null || filtroTipoActividad != null)
                                Icons.Default.FilterAltOff
                            else
                                Icons.Default.FilterList,
                            "Filtros"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Panel de filtros
            AnimatedVisibility(
                visible = showFiltros,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                FiltrosPanel(
                    filtroEstado = filtroEstado,
                    filtroTipoActividad = filtroTipoActividad,
                    onEstadoChange = viewModel::setFiltroEstado,
                    onTipoActividadChange = viewModel::setFiltroTipoActividad,
                    onLimpiarFiltros = viewModel::limpiarFiltros
                )
            }

            // Contenido principal
            when (val state = citacionesState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    state.data?.let { citaciones ->
                        if (citaciones.isEmpty()) {
                            EmptyStateContent()
                        } else {
                            CitacionesList(
                                citaciones = citaciones,
                                onCitacionClick = onNavigateToDetalle,
                                onConfirmarAsistencia = viewModel::confirmarAsistencia,
                                onRechazarAsistencia = viewModel::rechazarAsistencia
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    ErrorContent(
                        message = state.message ?: "Error al cargar citaciones",
                        onRetry = viewModel::loadCitaciones
                    )
                }
                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltrosPanel(
    filtroEstado: EstadoCitacion?,
    filtroTipoActividad: TipoActividad?,
    onEstadoChange: (EstadoCitacion?) -> Unit,
    onTipoActividadChange: (TipoActividad?) -> Unit,
    onLimpiarFiltros: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Filtros",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filtro por estado
            Text("Estado", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EstadoCitacion.values().take(3).forEach { estado ->
                    FilterChip(
                        selected = filtroEstado == estado,
                        onClick = {
                            onEstadoChange(if (filtroEstado == estado) null else estado)
                        },
                        label = { Text(estado.displayName, style = MaterialTheme.typography.bodySmall) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filtro por tipo
            Text("Tipo de Actividad", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TipoActividad.values().take(4).forEach { tipo ->
                    FilterChip(
                        selected = filtroTipoActividad == tipo,
                        onClick = {
                            onTipoActividadChange(if (filtroTipoActividad == tipo) null else tipo)
                        },
                        label = { Text(tipo.displayName, style = MaterialTheme.typography.bodySmall) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filtroEstado != null || filtroTipoActividad != null) {
                TextButton(
                    onClick = onLimpiarFiltros,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Clear, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Limpiar filtros")
                }
            }
        }
    }
}

@Composable
private fun CitacionesList(
    citaciones: List<Citacion>,
    onCitacionClick: (Int) -> Unit,
    onConfirmarAsistencia: (Int) -> Unit,
    onRechazarAsistencia: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(citaciones, key = { it.id }) { citacion ->
            CitacionCard(
                citacion = citacion,
                onClick = { onCitacionClick(citacion.id) },
                onConfirmar = { onConfirmarAsistencia(citacion.id) },
                onRechazar = { onRechazarAsistencia(citacion.id) }
            )
        }
    }
}

@Composable
private fun CitacionCard(
    citacion: Citacion,
    onClick: () -> Unit,
    onConfirmar: () -> Unit,
    onRechazar: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", Locale("es", "ES"))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Encabezado con tipo y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = getTipoIcon(citacion.tipoActividad),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = citacion.tipoActividad.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                EstadoBadge(estado = citacion.estado)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Título
            Text(
                text = citacion.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción
            Text(
                text = citacion.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fecha y lugar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = citacion.fecha.format(dateFormatter) + " - " + citacion.fecha.format(timeFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = citacion.lugar,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Asistencia
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.People,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${citacion.asistentesConfirmados}/${citacion.asistentesRequeridos} confirmados",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Progreso de asistencia
                LinearProgressIndicator(
                    progress = citacion.asistentesConfirmados.toFloat() / citacion.asistentesRequeridos.toFloat(),
                    modifier = Modifier
                        .width(100.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
            }
        }
    }
}

@Composable
private fun EstadoBadge(estado: EstadoCitacion) {
    val (color, icon) = when (estado) {
        EstadoCitacion.PENDIENTE -> MaterialTheme.colorScheme.tertiary to Icons.Default.Schedule
        EstadoCitacion.CONFIRMADA -> MaterialTheme.colorScheme.primary to Icons.Default.CheckCircle
        EstadoCitacion.EN_CURSO -> MaterialTheme.colorScheme.secondary to Icons.Default.PlayArrow
        EstadoCitacion.COMPLETADA -> MaterialTheme.colorScheme.primary to Icons.Default.Done
        EstadoCitacion.CANCELADA -> MaterialTheme.colorScheme.error to Icons.Default.Cancel
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = estado.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
private fun EmptyStateContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.EventBusy,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No hay citaciones",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No se encontraron citaciones con los filtros aplicados",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

private fun getTipoIcon(tipo: TipoActividad) = when (tipo) {
    TipoActividad.ENTRENAMIENTO -> Icons.Default.FitnessCenter
    TipoActividad.GUARDIA -> Icons.Default.Shield
    TipoActividad.REUNION -> Icons.Default.Groups
    TipoActividad.CEREMONIA -> Icons.Default.EmojiEvents
    TipoActividad.EJERCICIO -> Icons.Default.DirectionsRun
    TipoActividad.OTRO -> Icons.Default.MoreHoriz
}

