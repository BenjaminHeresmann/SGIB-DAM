// Archivo de configuracion de Gradle para el modulo de aplicacion
// Este archivo define como se construye y compila la aplicacion Android

// Seccion de plugins: Son extensiones que agregan funcionalidad al proceso de construccion
plugins {
    // Plugin para aplicaciones Android: permite compilar y empaquetar la app
    id("com.android.application")
    // Plugin de Kotlin para Android: habilita el uso del lenguaje Kotlin
    id("org.jetbrains.kotlin.android")
    // Plugin de Hilt: framework de inyeccion de dependencias de Google
    // Permite crear componentes que se pueden inyectar automaticamente en otros componentes
    id("com.google.dagger.hilt.android")
    // Plugin KSP (Kotlin Symbol Processing): procesador de anotaciones mas rapido que KAPT
    // Usado por Hilt y Room para generar codigo en tiempo de compilacion
    id("com.google.devtools.ksp")
}

// Configuracion especifica de Android
android {
    // Espacio de nombres unico de la aplicacion: define el paquete base
    namespace = "com.bomberos.sgib"
    // Version del SDK de Android con el que se compila la app
    compileSdk = 34

    // Configuracion por defecto de la aplicacion
    defaultConfig {
        // Identificador unico de la aplicacion en Google Play Store
        applicationId = "com.bomberos.sgib"
        // Version minima de Android requerida (Android 8.0 Oreo)
        minSdk = 26
        // Version de Android objetivo (Android 14)
        targetSdk = 34
        // Numero de version interna: se incrementa con cada actualizacion
        versionCode = 1
        // Nombre de la version visible para usuarios
        versionName = "1.0"

        // Runner para pruebas instrumentadas (pruebas en dispositivo real o emulador)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Configuracion de vectores dibujables (iconos SVG)
        vectorDrawables {
            // Usa la libreria de soporte para compatibilidad con versiones antiguas
            useSupportLibrary = true
        }

        // URL del backend - COMENTADO: Actualmente trabajamos sin backend
        // Usamos datos locales almacenados en memoria (FakeDataSource)
        // buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:3002/api/\"")
        // Valor por defecto para que compile (no se usa en modo local)
        buildConfigField("String", "BASE_URL", "\"http://localhost:3002/api/\"")
    }

    // Tipos de construccion: diferentes configuraciones para desarrollo y produccion
    buildTypes {
        // Configuracion para version de lanzamiento (release)
        release {
            // Minificacion desactivada: no reduce el tamano del codigo
            isMinifyEnabled = false
            // Archivos de reglas ProGuard para ofuscar y optimizar codigo
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Opciones de compilacion de Java
    compileOptions {
        // Version de Java para compatibilidad de codigo fuente
        sourceCompatibility = JavaVersion.VERSION_17
        // Version de Java para bytecode generado
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Opciones especificas de Kotlin
    kotlinOptions {
        // Version de JVM objetivo para el codigo Kotlin
        jvmTarget = "17"
    }

    // Caracteristicas de construccion habilitadas
    buildFeatures {
        // Habilita Jetpack Compose: framework moderno de UI declarativa
        compose = true
        // Habilita la generacion de clase BuildConfig con constantes
        buildConfig = true
    }

    // Opciones del compilador de Compose
    composeOptions {
        // Version del compilador de Kotlin compatible con Compose
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    // Configuracion de empaquetado
    packaging {
        resources {
            // Excluye archivos duplicados de licencias para evitar conflictos
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Seccion de dependencias: librerías externas que usa la aplicacion
dependencies {
    // --- LIBRERIAS CORE DE ANDROID ---
    // Core KTX: extensiones de Kotlin para APIs de Android, facilita el uso de funciones comunes
    implementation("androidx.core:core-ktx:1.12.0")
    // Lifecycle: maneja el ciclo de vida de componentes (Activity, Fragment, ViewModel)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    // Activity Compose: integra Jetpack Compose con Activities
    implementation("androidx.activity:activity-compose:1.8.1")
    // SplashScreen: API moderna para pantallas de inicio con animaciones
    implementation("androidx.core:core-splashscreen:1.0.1")

    // --- JETPACK COMPOSE (UI DECLARATIVA) ---
    // BOM (Bill of Materials): gestiona versiones compatibles de librerias Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    // UI Base: componentes fundamentales de Compose
    implementation("androidx.compose.ui:ui")
    // UI Graphics: utilidades para dibujar y renderizar graficos
    implementation("androidx.compose.ui:ui-graphics")
    // UI Tooling Preview: previsualizacion de componentes en Android Studio
    implementation("androidx.compose.ui:ui-tooling-preview")
    // Material3: componentes de diseño Material Design 3
    implementation("androidx.compose.material3:material3")
    // Iconos extendidos: biblioteca completa de iconos Material
    implementation("androidx.compose.material:material-icons-extended")

    // --- NAVEGACION ---
    // Navigation Compose: sistema de navegacion entre pantallas en Compose
    // Conecta NavController con NavHost y permite pasar argumentos entre destinos
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // --- ARQUITECTURA Y CICLO DE VIDA ---
    // ViewModel con Compose: vincula ViewModels con componentes Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    // Runtime Compose: observa estados del ciclo de vida en Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // --- INYECCION DE DEPENDENCIAS (HILT) ---
    // Hilt: framework de inyeccion de dependencias basado en Dagger
    // Permite crear instancias de clases automaticamente sin constructores manuales
    implementation("com.google.dagger:hilt-android:2.48")
    // Compilador de Hilt: genera codigo necesario para la inyeccion de dependencias
    ksp("com.google.dagger:hilt-android-compiler:2.48")
    // Integracion de Hilt con Navigation Compose: inyecta ViewModels en composables
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // --- NETWORKING (RED) ---
    // Retrofit: cliente HTTP para llamadas a APIs REST
    // Simplifica las peticiones GET, POST, PUT, DELETE al backend
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Conversor Gson: convierte JSON a objetos Kotlin y viceversa
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp: cliente HTTP de bajo nivel usado por Retrofit
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // Logging Interceptor: intercepta y muestra logs de peticiones HTTP para debugging
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // --- ALMACENAMIENTO LOCAL ---
    // DataStore: almacena preferencias de usuario de forma asincrona
    // Reemplazo moderno de SharedPreferences, usado para guardar token de sesion
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // --- BASE DE DATOS LOCAL (ROOM) ---
    // Room Runtime: abstraccion de SQLite para base de datos local
    implementation("androidx.room:room-runtime:2.6.1")
    // Room KTX: extensiones de Kotlin para Room con soporte de coroutines
    implementation("androidx.room:room-ktx:2.6.1")
    // Room Compiler: genera codigo de implementacion de DAOs en tiempo de compilacion
    ksp("androidx.room:room-compiler:2.6.1")

    // --- CARGA DE IMAGENES ---
    // Coil: libreria para cargar imagenes desde URLs o recursos locales
    // Optimizada para Compose, maneja cache y transformaciones de imagenes
    implementation("io.coil-kt:coil-compose:2.5.0")

    // --- AUTENTICACION BIOMETRICA ---
    // Biometric: API para autenticacion con huella dactilar o reconocimiento facial
    // Usado en LoginScreen para login biometrico
    implementation("androidx.biometric:biometric:1.1.0")

    // --- CAMARA ---
    // CameraX: API moderna para captura de fotos y video
    // Camera2: implementacion de bajo nivel de la camara
    implementation("androidx.camera:camera-camera2:1.3.1")
    // Lifecycle: integra camara con el ciclo de vida de Android
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    // View: componentes de UI para previsualizar la camara
    implementation("androidx.camera:camera-view:1.3.1")

    // --- UTILIDADES COMPOSE (ACCOMPANIST) ---
    // Permissions: manejo simplificado de permisos en tiempo de ejecucion
    // Usado para solicitar permisos de camara y almacenamiento
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
    // System UI Controller: controla colores de barras de sistema
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    // --- SERIALIZACION JSON ---
    // Gson: libreria para convertir objetos a JSON y viceversa
    // Usado por Retrofit y para almacenamiento local de datos
    implementation("com.google.code.gson:gson:2.10.1")

    // --- TESTING (PRUEBAS) ---
    // JUnit: framework para pruebas unitarias
    testImplementation("junit:junit:4.13.2")
    // JUnit para Android: pruebas instrumentadas en dispositivo
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // Espresso: framework para pruebas de UI
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // BOM para pruebas de Compose
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    // Pruebas de UI en Compose
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    // Herramientas de debugging para UI de Compose
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Manifiesto de pruebas para Compose
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}



