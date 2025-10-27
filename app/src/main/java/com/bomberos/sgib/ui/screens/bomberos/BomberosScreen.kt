package com.bomberos.sgib.ui.screens.bomberos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
// import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
// import androidx.compose.material.pullrefresh.PullRefreshIndicator
// import androidx.compose.material.pullrefresh.pullRefresh
// import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bomberos.sgib.domain.model.Bombero
import com.bomberos.sgib.ui.components.BomberoCard

/**
 * Pantalla principal de lista de bomberos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BomberosScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetalle: (Int) -> Unit,
    onNavigateToCrear: () -> Unit,
    viewModel: BomberosViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showFiltros by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bomberos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    // Botón de filtros
                    IconButton(onClick = { showFiltros = !showFiltros }) {
                        Icon(Icons.Default.FilterList, "Filtros")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCrear,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Agregar bombero")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Barra de búsqueda
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Filtros (si están visibles)
            if (showFiltros) {
                FiltrosChips(
                    filtroActual = state.filtroEstado,
                    onFiltroChange = { viewModel.onFiltroEstadoChange(it) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Contenido
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    state.error != null -> {
                        ErrorMessage(
                            message = state.error!!,
                            onRetry = { viewModel.loadBomberos() },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    state.bomberos.isEmpty() -> {
                        EmptyState(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        BomberosList(
                            bomberos = state.bomberos,
                            onBomberoClick = onNavigateToDetalle,
                            isRefreshing = state.isRefreshing,
                            onRefresh = { viewModel.refresh() }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Barra de búsqueda
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Buscar por nombre...") },
        leadingIcon = {
            Icon(Icons.Default.Search, "Buscar")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, "Limpiar")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

/**
 * Chips de filtros
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosChips(
    filtroActual: String,
    onFiltroChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filtros = listOf("Todos", "Activo", "Licencia", "Inactivo")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filtros.forEach { filtro ->
            FilterChip(
                selected = filtroActual == filtro,
                onClick = { onFiltroChange(filtro) },
                label = { Text(filtro) },
                leadingIcon = if (filtroActual == filtro) {
                    { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) }
                } else null
            )
        }
    }
}

/**
 * Lista de bomberos
 */
@Composable
fun BomberosList(
    bomberos: List<Bombero>,
    onBomberoClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {}
) {
    // TODO: Agregar Pull-to-Refresh cuando se añada la dependencia de Material
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(bomberos, key = { it.id }) { bombero ->
            BomberoCard(
                bombero = bombero,
                onClick = { onBomberoClick(bombero.id) }
            )
        }
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
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Estado vacío
 */
@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.PersonSearch,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No se encontraron bomberos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Intenta con otros filtros o búsqueda",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

/**
 * Mensaje de error
 */
@Composable
fun ErrorMessage(
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

