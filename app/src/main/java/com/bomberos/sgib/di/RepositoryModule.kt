package com.bomberos.sgib.di

import com.bomberos.sgib.data.local.PreferencesManager
import com.bomberos.sgib.data.repository.AuthRepositoryLocal
import com.bomberos.sgib.data.repository.BomberoRepositoryLocal
import com.bomberos.sgib.data.repository.CitacionRepository
import com.bomberos.sgib.data.repository.CitacionRepositoryLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para repositorios
 * VERSIÓN SIN BACKEND - Usa datos locales
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        preferencesManager: PreferencesManager
    ): AuthRepositoryLocal {
        return AuthRepositoryLocal(preferencesManager)
    }

    @Provides
    @Singleton
    fun provideBomberoRepository(): BomberoRepositoryLocal {
        return BomberoRepositoryLocal()
    }

    @Provides
    @Singleton
    fun provideCitacionRepository(): CitacionRepository {
        return CitacionRepositoryLocal()
    }
}

