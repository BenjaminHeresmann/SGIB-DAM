package com.bomberos.sgib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bomberos.sgib.ui.navigation.NavGraph
import com.bomberos.sgib.ui.theme.BomberosTheme
import dagger.hilt.android.AndroidEntryPoint

// MainActivity: Activity principal de la aplicacion
// Es el punto de entrada de la interfaz de usuario
// Hereda de ComponentActivity que proporciona soporte para Jetpack Compose
// En Android, una Activity representa una pantalla con interfaz de usuario

// Anotacion AndroidEntryPoint: Marca esta clase para inyeccion de dependencias con Hilt
// Permite que Hilt inyecte ViewModels y otras dependencias en esta Activity
// Conecta con BomberosApp (que tiene HiltAndroidApp) para acceder al contenedor de dependencias
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Metodo onCreate: Se ejecuta cuando la Activity se crea por primera vez
    // Bundle savedInstanceState: contiene el estado guardado si la Activity se recrea
    // Es el primer metodo del ciclo de vida de una Activity
    override fun onCreate(savedInstanceState: Bundle?) {

        // Instalar SplashScreen: Muestra pantalla de inicio con el logo mientras la app carga
        // Debe llamarse antes de super.onCreate para interceptar el inicio de la Activity
        // El splash screen se define en res/drawable/splash_screen.xml y themes.xml
        installSplashScreen()

        // Llamar al onCreate del padre (ComponentActivity)
        // Inicializa la Activity y restaura el estado guardado si existe
        super.onCreate(savedInstanceState)

        // Habilitar modo edge-to-edge: La UI se extiende bajo las barras de sistema
        // Proporciona una experiencia visual mas moderna e inmersiva
        // La app dibuja contenido detras de la barra de estado y barra de navegacion
        enableEdgeToEdge()

        // setContent: Define el contenido de la Activity usando Jetpack Compose
        // Reemplaza el tradicional setContentView(R.layout.activity_main)
        // Todo el contenido UI se define de forma declarativa con funciones composables
        setContent {
            // BomberosTheme: Tema personalizado de la aplicacion
            // Define colores, tipografia, formas y otros estilos visuales
            // Conecta con los archivos en ui/theme (Color.kt, Type.kt, Theme.kt)
            BomberosTheme {
                // Surface: Contenedor base de Material Design
                // Proporciona elevacion, forma y color de fondo
                Surface(
                    // Modifier.fillMaxSize: Ocupa todo el espacio disponible
                    modifier = Modifier.fillMaxSize(),

                    // Color de fondo segun el tema (claro u oscuro)
                    color = MaterialTheme.colorScheme.background
                ) {
                    // NavGraph: Grafo de navegacion de la aplicacion
                    // Define todas las pantallas y rutas de navegacion
                    // Conecta con ui/navigation/NavGraph.kt que contiene todas las rutas
                    // Maneja la navegacion entre LoginScreen, DashboardScreen, BomberosScreen, etc.
                    NavGraph()
                }
            }
        }
    }
    // No se sobrescriben otros metodos del ciclo de vida (onStart, onResume, onPause, etc.)
    // porque Compose maneja el ciclo de vida automaticamente
}



