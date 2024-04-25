package com.rodcollab.gymmateapp.auth.di

import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.data.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsAuthRepository(impl: AuthRepositoryImpl) : AuthRepository
}