package com.bomberos.sgib.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

// Clase BiometricHelper: Helper para gestionar autenticacion biometrica
// Recurso Nativo 2: Huella digital o Face ID (reconocimiento facial)
// Encapsula la complejidad de la API BiometricPrompt de Android
// Proporciona metodos sencillos para verificar disponibilidad y mostrar el prompt de autenticacion

// Constructor: recibe el Context (contexto de la aplicacion)
// Context es necesario para acceder a servicios del sistema como BiometricManager
class BiometricHelper(private val context: Context) {

    // Funcion isBiometricAvailable: Verifica si el dispositivo tiene biometria disponible
    // Retorna: true si el dispositivo puede usar autenticacion biometrica, false si no
    fun isBiometricAvailable(): Boolean {
        // BiometricManager: servicio del sistema que gestiona capacidades biometricas
        // from(context) obtiene una instancia del manager para este contexto
        val biometricManager = BiometricManager.from(context)

        // canAuthenticate: verifica si el dispositivo puede autenticar con biometria
        // BiometricManager.Authenticators.BIOMETRIC_STRONG: autenticacion fuerte (huella, face id)
        // BiometricManager.Authenticators.BIOMETRIC_WEAK: autenticacion debil (reconocimiento facial basico)
        // El operador 'or' permite usar cualquiera de los dos tipos
        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        )) {
            // BIOMETRIC_SUCCESS: el dispositivo tiene biometria y esta configurada
            BiometricManager.BIOMETRIC_SUCCESS -> true

            // BIOMETRIC_ERROR_NO_HARDWARE: el dispositivo no tiene sensor biometrico
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false

            // BIOMETRIC_ERROR_HW_UNAVAILABLE: tiene hardware pero no esta disponible ahora
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false

            // BIOMETRIC_ERROR_NONE_ENROLLED: tiene hardware pero el usuario no ha registrado huellas
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false

            // Cualquier otro caso desconocido
            else -> false
        }
    }
    // Se conecta con:
    // - LoginScreen: verifica si puede mostrar el boton de autenticacion biometrica

    // Funcion getBiometricStatus: Obtiene mensaje descriptivo del estado de biometria
    // Retorna: String con mensaje legible del estado actual
    // Util para mostrar al usuario por que no puede usar biometria
    fun getBiometricStatus(): String {
        val biometricManager = BiometricManager.from(context)

        // Similar a isBiometricAvailable pero retorna mensajes descriptivos
        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> "Biometría disponible"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "No hay hardware biométrico"
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Hardware no disponible"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "No hay huellas registradas"
            else -> "Estado desconocido"
        }
    }
    // Se conecta con:
    // - LoginScreen: muestra mensajes de error si la biometria no esta disponible

    // Funcion showBiometricPrompt: Muestra el dialogo de autenticacion biometrica
    // Es el metodo principal que invoca el sensor de huella o face id
    // Parametros personalizables para diferentes contextos de uso
    fun showBiometricPrompt(
        // activity: Activity que invoca el prompt (necesaria para mostrar el dialogo)
        activity: FragmentActivity,

        // title: Titulo del dialogo de autenticacion
        title: String = "Autenticación Biométrica",

        // subtitle: Subtitulo con instrucciones adicionales
        subtitle: String = "Verifica tu identidad",

        // negativeButtonText: Texto del boton para cancelar o usar metodo alternativo
        negativeButtonText: String = "Usar contraseña",

        // onSuccess: Callback que se ejecuta cuando la autenticacion es exitosa
        // Se llama cuando el usuario escanea su huella correctamente
        onSuccess: () -> Unit,

        // onError: Callback que se ejecuta cuando hay un error
        // Recibe un String con la descripcion del error
        // Ejemplos: "Demasiados intentos", "Sensor no disponible"
        onError: (String) -> Unit,

        // onFailed: Callback que se ejecuta cuando la autenticacion falla
        // Se llama cuando el sensor no reconoce la huella
        // No es un error critico, el usuario puede intentar de nuevo
        onFailed: () -> Unit
    ) {
        // Executor: hilo en el que se ejecutaran los callbacks
        // getMainExecutor: usa el hilo principal (UI thread) para actualizar la interfaz
        val executor = ContextCompat.getMainExecutor(activity)

        // BiometricPrompt: dialogo del sistema para autenticacion biometrica
        // Se construye con la activity, el executor y los callbacks
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            // AuthenticationCallback: objeto que maneja los resultados de la autenticacion
            object : BiometricPrompt.AuthenticationCallback() {
                // onAuthenticationError: se llama cuando hay un error que impide la autenticacion
                // errorCode: codigo numerico del error
                // errString: descripcion legible del error
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Ejecuta el callback onError con el mensaje de error
                    onError(errString.toString())
                }

                // onAuthenticationSucceeded: se llama cuando la autenticacion es exitosa
                // result: resultado de la autenticacion (incluye cryptoObject si se uso)
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Ejecuta el callback onSuccess
                    onSuccess()
                }

                // onAuthenticationFailed: se llama cuando el sensor no reconoce la huella
                // El usuario puede intentar de nuevo
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Ejecuta el callback onFailed
                    onFailed()
                }
            }
        )

        // PromptInfo: configuracion del dialogo de autenticacion
        // Define titulos, subtitulos y botones del dialogo
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)              // Titulo principal del dialogo
            .setSubtitle(subtitle)        // Subtitulo con instrucciones
            .setNegativeButtonText(negativeButtonText)  // Boton para cancelar
            .build()

        // authenticate: muestra el dialogo y activa el sensor biometrico
        // El usuario debe escanear su huella o rostro para autenticarse
        biometricPrompt.authenticate(promptInfo)
    }
    // Se conecta con:
    // - LoginScreen: invoca showBiometricPrompt cuando el usuario toca el boton de huella
    // - Si la autenticacion es exitosa, hace login automaticamente
}
// Este helper centraliza toda la logica de biometria para facilitar su uso
// Evita duplicar codigo en multiples pantallas


