package com.rodcollab.gymmateapp.core.di

import android.app.Application
import com.rodcollab.gymmateapp.core.data.AppDatabase
import com.rodcollab.gymmateapp.core.data.dao.BodyPartDao
import com.rodcollab.gymmateapp.core.data.dao.ExerciseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalStorageModule {
    @Singleton
    @Provides
    fun providesLocalDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun providesExerciseDao(database: AppDatabase): ExerciseDao {
        return database.exerciseDao()
    }
    @Singleton
    @Provides
    fun providesBodyPartDao(database: AppDatabase): BodyPartDao {
        return database.bodyPartDao()
    }
}