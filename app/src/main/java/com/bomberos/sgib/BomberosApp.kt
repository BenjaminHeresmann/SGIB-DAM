package com.bomberos.sgib

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Clase Application personalizada: Es la primera clase que se instancia cuando la app inicia
// Se ejecuta antes que cualquier Activity o componente
// Su ciclo de vida es el mismo que el de toda la aplicacion
// Se usa para inicializaciones globales y configuraciones que deben estar disponibles en toda la app

// Anotacion HiltAndroidApp: Marca esta clase como el punto de entrada de Hilt
// Hilt es un framework de inyeccion de dependencias que:
// 1. Crea un contenedor de dependencias a nivel de aplicacion
// 2. Genera codigo necesario para inyectar dependencias automaticamente
// 3. Gestiona el ciclo de vida de las dependencias
// Esta anotacion debe estar en la clase Application para que Hilt funcione
// Conecta con los modulos de Hilt definidos en el paquete di (AppModule, NetworkModule, RepositoryModule)
@HiltAndroidApp
class BomberosApp : Application() {
    // No necesita codigo adicional por ahora
    // Hilt se encarga de toda la configuracion automaticamente
    // Si se necesitaran inicializaciones globales (como inicializar librerias de logging,
    // analytics, o configuraciones de base de datos), se harian en el metodo onCreate()
}


