package com.rodcollab.gymmateapp.core.navigation

import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.SIGN_PATH
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.addOrEditArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.bodyPartArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.emailArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.exerciseIdArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.passwordArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.ADD_OR_EDIT_EXERCISE_SCREEN
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.ADD_TRAINING
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.AUTH_SCREEN
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.EXERCISES
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.EXERCISE_DETAILS
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.TRAININGS

/**
 * Screens used in [GymMateDestinations]
 */
internal object GymMateScreens {
    const val MAIN_SCREEN = "main"
    const val AUTH_SCREEN = "authentication"
    const val EXERCISES = "exercises"
    const val ADD_OR_EDIT_EXERCISE_SCREEN = "addOrEditExerciseScreen"
    const val TRAININGS = "trainings"
    const val ADD_TRAINING = "addTraining"
    const val EXERCISE_DETAILS = "exerciseDetails"
}

/**
 * Arguments used in [GymMateDestinations] routes
 */
object GymMateDestinationsArgs {
    const val SIGN_PATH = "signPath"
    const val emailArgs = "email"
    const val passwordArgs = "password"
    const val addOrEditArgs = "addOrEdit"
    const val bodyPartArgs = "bodyPart"
    const val exerciseIdArgs = "exerciseId"
    const val nameExerciseArgs = "nameExercise"
    const val imgUrlExerciseArgs = "imgUrl"
    const val notesExerciseArgs = "notes"
}

/**
 * Destinations used in the [MainActivity]
 */
object GymMateDestinations {
    const val SIGNUP_ROUTE = "$AUTH_SCREEN/{$SIGN_PATH}"
    const val SIGNIN_ROUTE = "$AUTH_SCREEN/{$SIGN_PATH}?$emailArgs={$emailArgs}?$passwordArgs={$passwordArgs}"
    const val ADD_OR_EDIT_EXERCISE_ROUTE = "$ADD_OR_EDIT_EXERCISE_SCREEN/{$addOrEditArgs}?$bodyPartArgs={$bodyPartArgs}?$exerciseIdArgs={$exerciseIdArgs}"
    const val TRAINING_ROUTE = TRAININGS
    const val ADD_TRAINING_ROUTE = ADD_TRAINING
    const val EXERCISE_DETAILS_ROUTE = "$EXERCISE_DETAILS/{$exerciseIdArgs}"
    const val EXERCISES_ROUTE = "$EXERCISES/{$bodyPartArgs}"
}