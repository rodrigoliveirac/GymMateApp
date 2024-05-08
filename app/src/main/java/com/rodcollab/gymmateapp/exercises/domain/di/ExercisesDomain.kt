package com.rodcollab.gymmateapp.exercises.domain.di

import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository
import com.rodcollab.gymmateapp.exercises.domain.AddExerciseImpl
import com.rodcollab.gymmateapp.exercises.domain.ExercisesByBPImpl
import com.rodcollab.gymmateapp.exercises.domain.ReadExerciseImpl
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object ExercisesDomainModule {

    @ViewModelScoped
    @Provides
    fun providesExercisesDomain(
        exercisesRepository: ExercisesRepository,
    ): ExercisesDomain {
        return ExercisesDomain(
            readExercise = ReadExerciseImpl(exercisesRepository),
            exercises = ExercisesByBPImpl(
                exercises = exercisesRepository,
            ),
            addExercise = AddExerciseImpl(exercisesRepository)
        )
    }
}