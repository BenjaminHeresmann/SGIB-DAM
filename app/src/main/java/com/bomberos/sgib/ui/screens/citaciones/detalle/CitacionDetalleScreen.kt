package com.bomberos.sgib.ui.screens.citaciones.detalle

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bomberos.sgib.domain.model.Citacion
import com.bomberos.sgib.domain.model.EstadoCitacion
import com.bomberos.sgib.domain.model.TipoActividad
import com.bomberos.sgib.util.NotificationHelper
import com.bomberos.sgib.util.Resource
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Pantalla de detalle de una citación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitacionDetalleScreen(
    onNavigateBack: () -> Unit,
    viewModel: CitacionDetalleViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }
    val citacionState by viewModel.citacionState.collectAsState()
    val confirmacionState by viewModel.confirmacionState.collectAsState()
    
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Citación") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = citacionState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    state.data?.let { citacion ->
                        CitacionDetalleContent(
                            citacion = citacion,
                            onConfirmar = { showConfirmDialog = true },
                            onRechazar = { showRejectDialog = true }
                        )
                    }
                }
                is Resource.Error -> {
                    ErrorContent(
                        message = state.message ?: "Error al cargar citación",
                        onRetry = viewModel::loadCitacion
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

        // Diálogos de confirmación
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                icon = { Icon(Icons.Default.CheckCircle, null) },
                title = { Text("Confirmar Asistencia") },
                text = { Text("¿Confirmas que asistirás a esta citación?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.confirmarAsistencia()
                            showConfirmDialog = false
                        }
                    ) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showRejectDialog) {
            AlertDialog(
                onDismissRequest = { showRejectDialog = false },
                icon = { Icon(Icons.Default.Cancel, null) },
                title = { Text("Rechazar Asistencia") },
                text = { Text("¿Seguro que no podrás asistir a esta citación?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.rechazarAsistencia()
                            showRejectDialog = false
                        }
                    ) {
                        Text("No Asistiré")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRejectDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Snackbar de confirmación
        LaunchedEffect(confirmacionState) {
            if (confirmacionState is Resource.Success) {
                val citacion = (confirmacionState as Resource.Success<Citacion>).data
                if (citacion != null) {
                    notificationHelper.showConfirmacionAsistencia(citacion)
                }
                viewModel.clearConfirmacionState()
            }
        }
    }
}

@Composable
private fun CitacionDetalleContent(
    citacion: Citacion,
    onConfirmar: () -> Unit,
    onRechazar: () -> Unit
) {
    val scrollState = rememberScrollState()
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM yyyy", Locale("es", "ES"))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header con tipo y estado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = getTipoColor(citacion.tipoActividad).copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = getTipoIcon(citacion.tipoActividad),
                            contentDescription = null,
                            tint = getTipoColor(citacion.tipoActividad),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = citacion.tipoActividad.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = getTipoColor(citacion.tipoActividad),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    EstadoBadge(estado = citacion.estado)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = citacion.titulo,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Descripción",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = citacion.descripcion,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fecha y Hora
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Fecha",
                    value = citacion.fecha.format(dateFormatter).replaceFirstChar { it.uppercase() }
                )
                Spacer(modifier = Modifier.height(12.dp))
                InfoRow(
                    icon = Icons.Default.AccessTime,
                    label = "Hora",
                    value = citacion.fecha.format(timeFormatter) + " hrs"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lugar
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow(
                    icon = Icons.Default.LocationOn,
                    label = "Lugar",
                    value = citacion.lugar
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Asistencia
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.People,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Asistencia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${citacion.asistentesConfirmados} de ${citacion.asistentesRequeridos}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Confirmados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        val porcentaje = (citacion.asistentesConfirmados.toFloat() / citacion.asistentesRequeridos.toFloat() * 100).toInt()
                        Text(
                            text = "$porcentaje%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = citacion.asistentesConfirmados.toFloat() / citacion.asistentesRequeridos.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }

        // Observaciones (si existen)
        citacion.observaciones?.let { obs ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Observaciones",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = obs,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Información adicional
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Información Adicional",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                InfoRow(
                    icon = Icons.Default.Person,
                    label = "Creado por",
                    value = citacion.creadoPor
                )
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(
                    icon = Icons.Default.Schedule,
                    label = "Fecha de creación",
                    value = citacion.fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botones de acción (solo si está pendiente)
        if (citacion.estado == EstadoCitacion.PENDIENTE || citacion.estado == EstadoCitacion.CONFIRMADA) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRechazar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Cancel, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("No Asistiré")
                }

                Button(
                    onClick = onConfirmar,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Confirmar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
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
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = estado.displayName,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
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

@Composable
private fun getTipoColor(tipo: TipoActividad) = when (tipo) {
    TipoActividad.ENTRENAMIENTO -> MaterialTheme.colorScheme.primary
    TipoActividad.GUARDIA -> MaterialTheme.colorScheme.secondary
    TipoActividad.REUNION -> MaterialTheme.colorScheme.tertiary
    TipoActividad.CEREMONIA -> MaterialTheme.colorScheme.error
    TipoActividad.EJERCICIO -> MaterialTheme.colorScheme.primary
    TipoActividad.OTRO -> MaterialTheme.colorScheme.onSurfaceVariant
}

