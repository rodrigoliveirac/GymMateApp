package com.rodcollab.gymmateapp.core.di

import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun providesExercisesRepository(impl: ExercisesRepositoryImpl): ExercisesRepository
    
}