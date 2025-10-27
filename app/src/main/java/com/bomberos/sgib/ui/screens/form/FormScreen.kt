package com.bomberos.sgib.ui.screens.form

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter

/**
 * Pantalla de formulario para crear/editar bombero
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onNavigateBack: () -> Unit,
    viewModel: FormViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showFotoOptions by remember { mutableStateOf(false) }

    // Launcher para seleccionar imagen de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateFoto(it.toString()) }
    }

    // Launcher para tomar foto con cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // La foto se guardó exitosamente
            // El URI ya fue configurado antes de lanzar la cámara
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (state.isEditMode) "Editar Bombero" else "Nuevo Bombero"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección de foto
            FotoSection(
                fotoUri = state.fotoUri,
                onClick = { showFotoOptions = true }
            )

            // Información personal
            SectionTitle(
                title = "Información Personal",
                icon = Icons.Default.Person
            )

            FormTextField(
                value = state.nombres,
                onValueChange = { viewModel.updateField(FormField.NOMBRES, it) },
                label = "Nombres *",
                error = state.errors[FormField.NOMBRES],
                leadingIcon = Icons.Default.Person
            )

            FormTextField(
                value = state.apellidos,
                onValueChange = { viewModel.updateField(FormField.APELLIDOS, it) },
                label = "Apellidos *",
                error = state.errors[FormField.APELLIDOS],
                leadingIcon = Icons.Default.Person
            )

            FormTextField(
                value = state.email,
                onValueChange = { viewModel.updateField(FormField.EMAIL, it) },
                label = "Email",
                error = state.errors[FormField.EMAIL],
                leadingIcon = Icons.Default.Email
            )

            FormTextField(
                value = state.telefono,
                onValueChange = { viewModel.updateField(FormField.TELEFONO, it) },
                label = "Teléfono",
                error = state.errors[FormField.TELEFONO],
                leadingIcon = Icons.Default.Phone
            )

            FormTextField(
                value = state.direccion,
                onValueChange = { viewModel.updateField(FormField.DIRECCION, it) },
                label = "Dirección",
                error = state.errors[FormField.DIRECCION],
                leadingIcon = Icons.Default.Home,
                maxLines = 3
            )

            // Información laboral
            SectionTitle(
                title = "Información Laboral",
                icon = Icons.Default.WorkOutline
            )

            RangoDropdown(
                selectedRango = state.rango,
                onRangoSelected = { viewModel.updateField(FormField.RANGO, it) },
                error = state.errors[FormField.RANGO]
            )

            FormTextField(
                value = state.especialidad,
                onValueChange = { viewModel.updateField(FormField.ESPECIALIDAD, it) },
                label = "Especialidad",
                error = state.errors[FormField.ESPECIALIDAD],
                leadingIcon = Icons.Default.Star
            )

            EstadoDropdown(
                selectedEstado = state.estado,
                onEstadoSelected = { viewModel.updateField(FormField.ESTADO, it) },
                error = state.errors[FormField.ESTADO]
            )

            // Mensaje de error general
            if (state.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Botón de guardar
            Button(
                onClick = { viewModel.guardar(onNavigateBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Save, "Guardar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state.isEditMode) "Actualizar" else "Crear",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Texto de campos requeridos
            Text(
                text = "* Campos requeridos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }

    // Bottom sheet para opciones de foto
    if (showFotoOptions) {
        ModalBottomSheet(
            onDismissRequest = { showFotoOptions = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Seleccionar foto",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Opción: Tomar foto
                ListItem(
                    headlineContent = { Text("Tomar foto") },
                    leadingContent = {
                        Icon(Icons.Default.CameraAlt, "Cámara")
                    },
                    modifier = Modifier.clickable {
                        showFotoOptions = false
                        // TODO: Lanzar cámara
                    }
                )

                // Opción: Seleccionar de galería
                ListItem(
                    headlineContent = { Text("Seleccionar de galería") },
                    leadingContent = {
                        Icon(Icons.Default.PhotoLibrary, "Galería")
                    },
                    modifier = Modifier.clickable {
                        showFotoOptions = false
                        galleryLauncher.launch("image/*")
                    }
                )

                // Opción: Eliminar foto (si hay una)
                if (state.fotoUri != null) {
                    ListItem(
                        headlineContent = { Text("Eliminar foto") },
                        leadingContent = {
                            Icon(
                                Icons.Default.Delete,
                                "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        modifier = Modifier.clickable {
                            viewModel.updateFoto(null)
                            showFotoOptions = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Sección de foto
 */
@Composable
fun FotoSection(
    fotoUri: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (fotoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(fotoUri),
                        contentDescription = "Foto del bombero",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Agregar foto",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (fotoUri != null) "Cambiar foto" else "Agregar foto",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Título de sección
 */
@Composable
fun SectionTitle(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
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
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Campo de texto del formulario
 */
@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = leadingIcon?.let {
            { Icon(it, contentDescription = null) }
        },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        maxLines = maxLines,
        shape = RoundedCornerShape(12.dp)
    )
}

/**
 * Dropdown de rango
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangoDropdown(
    selectedRango: String,
    onRangoSelected: (String) -> Unit,
    error: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val rangos = listOf(
        "Bombero",
        "Cabo",
        "Sargento",
        "Teniente",
        "Capitán",
        "Comandante"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedRango,
            onValueChange = {},
            readOnly = true,
            label = { Text("Rango *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            rangos.forEach { rango ->
                DropdownMenuItem(
                    text = { Text(rango) },
                    onClick = {
                        onRangoSelected(rango)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Dropdown de estado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadoDropdown(
    selectedEstado: String,
    onEstadoSelected: (String) -> Unit,
    error: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val estados = listOf("Activo", "Licencia", "Inactivo")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedEstado,
            onValueChange = {},
            readOnly = true,
            label = { Text("Estado *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            leadingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            estados.forEach { estado ->
                DropdownMenuItem(
                    text = { Text(estado) },
                    onClick = {
                        onEstadoSelected(estado)
                        expanded = false
                    }
                )
            }
        }
    }
}

