package com.bomberos.sgib.di

import android.content.Context
import com.bomberos.sgib.data.local.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Modulo AppModule: Modulo de Hilt para dependencias a nivel de aplicacion
// La inyeccion de dependencias permite crear instancias de clases automaticamente
// sin tener que construirlas manualmente en cada lugar donde se necesiten
// Hilt gestiona el ciclo de vida y garantiza que las dependencias esten disponibles

// Anotacion Module: Marca esta clase como un modulo de Hilt
// Un modulo es una clase que le dice a Hilt como crear instancias de ciertos tipos
@Module

// Anotacion InstallIn: Define el alcance o ciclo de vida del modulo
// SingletonComponent: Este modulo vive mientras viva la aplicacion
// Las dependencias creadas aqui son singleton (una sola instancia para toda la app)
@InstallIn(SingletonComponent::class)

// Object: Singleton de Kotlin, solo existe una instancia de este modulo
object AppModule {

    // Anotacion Provides: Indica que este metodo proporciona una dependencia
    // Hilt llamara a este metodo cuando necesite una instancia de PreferencesManager
    @Provides

    // Anotacion Singleton: La instancia creada sera reutilizada en toda la app
    // Solo se creara una vez y se compartira entre todos los componentes que la necesiten
    // Esto es importante para PreferencesManager porque debe ser unico para mantener consistencia
    @Singleton
    fun providePreferencesManager(
        // Parametro ApplicationContext: El contexto de la aplicacion
        // ApplicationContext es inyectado automaticamente por Hilt
        // Es el contexto global de la app, vive mientras la app este en ejecucion
        @ApplicationContext context: Context
    ): PreferencesManager {
        // Crea y retorna una nueva instancia de PreferencesManager
        // PreferencesManager necesita el contexto para acceder a DataStore
        // Esta instancia sera reutilizada en todos los lugares donde se inyecte PreferencesManager
        return PreferencesManager(context)
    }
    // Este metodo se conecta con:
    // - AuthRepository: inyecta PreferencesManager para guardar token y datos de usuario
    // - LoginViewModel: a traves de AuthRepository, usa PreferencesManager para login
    // - DashboardViewModel: a traves de AuthRepository, usa PreferencesManager para obtener usuario logueado
}
// Este modulo forma parte del sistema de inyeccion de dependencias junto con:
// - NetworkModule: proporciona dependencias de red (Retrofit, OkHttp)
// - RepositoryModule: proporciona instancias de repositorios
// - BomberosApp (con HiltAndroidApp): punto de entrada de Hilt
// - MainActivity (con AndroidEntryPoint): permite inyectar dependencias en la Activity


