package com.bomberos.sgib.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bomberos.sgib.R
import com.bomberos.sgib.domain.model.Stats
import com.bomberos.sgib.util.Resource
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Pantalla principal del Dashboard
 * Muestra estadísticas generales del sistema
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToBomberos: () -> Unit,
    onNavigateToCitaciones: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val statsState by viewModel.statsState.collectAsState()
    val scrollState = rememberScrollState()

    // Configurar color de status bar
    val systemUiController = rememberSystemUiController()
    val primaryColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = false
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Dashboard",
                            style = MaterialTheme.typography.titleLarge
                        )
                        currentUser?.let { user ->
                            Text(
                                text = "Bienvenido, ${user.nombre}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = statsState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    state.data?.let { stats ->
                        DashboardContent(
                            stats = stats,
                            onNavigateToBomberos = onNavigateToBomberos,
                            onNavigateToCitaciones = onNavigateToCitaciones,
                            onRefresh = viewModel::refresh,
                            scrollState = scrollState
                        )
                    }
                }
                is Resource.Error -> {
                    ErrorContent(
                        message = state.message ?: "Error al cargar estadísticas",
                        onRetry = viewModel::refresh
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

@Composable
private fun DashboardContent(
    stats: Stats,
    onNavigateToBomberos: () -> Unit,
    onNavigateToCitaciones: () -> Unit,
    onRefresh: () -> Unit,
    scrollState: androidx.compose.foundation.ScrollState
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Tarjeta de bienvenida
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo oficial Segunda Compañía
                    Image(
                        painter = painterResource(R.drawable.logo_bomberos),
                        contentDescription = "Logo Segunda Compañía Bomberos Viña del Mar",
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Sistema de Bomberos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Segunda Compañía Viña del Mar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Grid de estadísticas
        Text(
            text = "Estadísticas Generales",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Primera fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Total",
                value = stats.total.toString(),
                icon = Icons.Default.Group,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
                delay = 100
            )
            StatCard(
                title = "Activos",
                value = stats.totalActivos.toString(),
                icon = Icons.Default.CheckCircle,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f),
                delay = 200
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Segunda fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Licencia",
                value = stats.totalInactivos.toString(),
                icon = Icons.Default.AccessTime,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f),
                delay = 300
            )
            StatCard(
                title = "Nuevos",
                value = stats.nuevosUltimoMes.toString(),
                icon = Icons.Default.PersonAdd,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f),
                delay = 400
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botones de acción
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it })
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateToBomberos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver Lista de Bomberos")
                }

                OutlinedButton(
                    onClick = onNavigateToCitaciones,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver Citaciones")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Distribución por rangos (si hay datos)
        if (stats.porRango.isNotEmpty()) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Distribución por Rangos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        stats.porRango.forEach { rangoCount ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = rangoCount.rango,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = rangoCount.cantidad.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    delay: Int = 0
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reintentar")
        }
    }
}

