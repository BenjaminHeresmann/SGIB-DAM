package com.bomberos.sgib.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.rememberAsyncImagePainter

/**
 * Componente reutilizable para seleccionar foto de cámara o galería
 * Recurso Nativo 1: Acceso a Cámara y Galería
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPicker(
    photoUri: String?,
    onPhotoSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
    showCamera: Boolean = true,
    title: String = "Seleccionar foto"
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    // Launcher para galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onPhotoSelected(it.toString()) }
    }

    // Launcher para cámara (simplificado)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // La foto se guardó exitosamente en el URI temporal
        }
    }

    // Card principal clickeable
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showBottomSheet = true },
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
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Foto seleccionada",
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
                text = if (photoUri != null) "Cambiar foto" else "Agregar foto",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }

    // Bottom sheet con opciones
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Opción: Tomar foto
                if (showCamera) {
                    ListItem(
                        headlineContent = { Text("Tomar foto") },
                        leadingContent = {
                            Icon(Icons.Default.CameraAlt, "Cámara")
                        },
                        modifier = Modifier.clickable {
                            showBottomSheet = false
                            // TODO: Implementar cámara con URI temporal
                            // Por ahora, mostrar que está en desarrollo
                        }
                    )
                }

                // Opción: Seleccionar de galería
                ListItem(
                    headlineContent = { Text("Seleccionar de galería") },
                    leadingContent = {
                        Icon(Icons.Default.PhotoLibrary, "Galería")
                    },
                    modifier = Modifier.clickable {
                        showBottomSheet = false
                        galleryLauncher.launch("image/*")
                    }
                )

                // Opción: Eliminar foto (si hay una)
                if (photoUri != null) {
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
                            onPhotoSelected(null)
                            showBottomSheet = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

