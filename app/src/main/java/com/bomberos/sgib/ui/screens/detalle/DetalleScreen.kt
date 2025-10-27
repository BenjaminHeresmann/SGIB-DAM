package com.bomberos.sgib.ui.screens.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bomberos.sgib.domain.model.Bombero

/**
 * Pantalla de detalle de bombero
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEditar: (Int) -> Unit,
    viewModel: DetalleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Bombero") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    state.bombero?.let { bombero ->
                        IconButton(onClick = { onNavigateToEditar(bombero.id) }) {
                            Icon(Icons.Default.Edit, "Editar")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, "Eliminar")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    ErrorContent(
                        message = state.error!!,
                        onRetry = { viewModel.reload() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.bombero != null -> {
                    DetalleContent(bombero = state.bombero!!)
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Bombero") },
            text = { Text("¿Estás seguro de que deseas eliminar este bombero? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // TODO: Implementar eliminación
                        showDeleteDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Contenido principal del detalle
 */
@Composable
fun DetalleContent(
    bombero: Bombero,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header con avatar y nombre
        HeaderCard(bombero = bombero)

        // Información personal
        InfoSection(
            title = "Información Personal",
            icon = Icons.Default.Person
        ) {
            InfoRow(label = "Nombres", value = bombero.nombres)
            InfoRow(label = "Apellidos", value = bombero.apellidos)
            if (bombero.email != null) {
                InfoRow(label = "Email", value = bombero.email)
            }
            if (bombero.telefono != null) {
                InfoRow(label = "Teléfono", value = bombero.telefono)
            }
            if (bombero.direccion != null) {
                InfoRow(label = "Dirección", value = bombero.direccion)
            }
        }

        // Información laboral
        InfoSection(
            title = "Información Laboral",
            icon = Icons.Default.WorkOutline
        ) {
            InfoRow(label = "Rango", value = bombero.rango)
            if (bombero.especialidad != null) {
                InfoRow(label = "Especialidad", value = bombero.especialidad)
            }
            InfoRow(label = "Estado", value = bombero.estado)
            if (bombero.fechaIngreso != null) {
                InfoRow(label = "Fecha de Ingreso", value = bombero.fechaIngreso)
            }
        }

        // Información del sistema
        InfoSection(
            title = "Información del Sistema",
            icon = Icons.Default.Info
        ) {
            InfoRow(label = "ID", value = bombero.id.toString())
            InfoRow(label = "Creado", value = bombero.createdAt)
            InfoRow(label = "Actualizado", value = bombero.updatedAt)
        }
    }
}

/**
 * Card del header con avatar
 */
@Composable
fun HeaderCard(bombero: Bombero) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar grande
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = bombero.nombres.first().toString() + bombero.apellidos.first().toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre completo
            Text(
                text = bombero.nombreCompleto,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Rango
            Text(
                text = bombero.rango,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Badge de estado
            EstadoBadge(estado = bombero.estado)
        }
    }
}

/**
 * Sección de información
 */
@Composable
fun InfoSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título de la sección
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Divider()

            Spacer(modifier = Modifier.height(12.dp))

            // Contenido
            content()
        }
    }
}

/**
 * Fila de información
 */
@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Badge de estado
 */
@Composable
fun EstadoBadge(estado: String) {
    val (color, text) = when (estado) {
        "Activo" -> Color(0xFF4CAF50) to "Activo"
        "Licencia" -> Color(0xFFFF9800) to "Licencia"
        "Inactivo" -> Color(0xFFF44336) to "Inactivo"
        else -> Color.Gray to estado
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Contenido de error
 */
@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
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
            text = "Error",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Icon(Icons.Default.Refresh, "Reintentar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reintentar")
        }
    }
}

