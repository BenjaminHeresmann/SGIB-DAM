package com.bomberos.sgib.di

import com.bomberos.sgib.data.local.PreferencesManager
import com.bomberos.sgib.data.remote.ApiService
import com.bomberos.sgib.data.repository.AuthRepository
import com.bomberos.sgib.data.repository.BomberoRepository
import com.bomberos.sgib.data.repository.CitacionRepository
import com.bomberos.sgib.data.repository.CitacionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para repositorios
 * Conectados al backend en Railway
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: ApiService,
        preferencesManager: PreferencesManager
    ): AuthRepository {
        return AuthRepository(api, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideBomberoRepository(
        api: ApiService
    ): BomberoRepository {
        return BomberoRepository(api)
    }

    @Provides
    @Singleton
    fun provideCitacionRepository(
        api: ApiService
    ): CitacionRepository {
        return CitacionRepositoryImpl(api)
    }
}

