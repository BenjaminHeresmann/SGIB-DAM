package com.bomberos.sgib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bomberos.sgib.ui.navigation.NavGraph
import com.bomberos.sgib.ui.theme.BomberosTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity principal de la aplicación
 * Configurada con Hilt y Jetpack Compose
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar edge-to-edge para mejor experiencia visual
        enableEdgeToEdge()

        setContent {
            BomberosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}

