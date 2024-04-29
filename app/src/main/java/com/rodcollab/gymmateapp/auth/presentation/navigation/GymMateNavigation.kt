package com.rodcollab.gymmateapp.auth.presentation.navigation

import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.SIGN_PATH
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.addOrEditArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.bodyPartArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.emailArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.imgUrlExerciseArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.nameExerciseArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.notesExerciseArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.passwordArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens.ADD_OR_EDIT_EXERCISE_SCREEN
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens.AUTH_SCREEN

/**
 * Screens used in [GymMateDestinations]
 */
internal object GymMateScreens {
    const val MAIN_SCREEN = "main"
    const val AUTH_SCREEN = "authentication"
    const val EXERCISES = "exercises"
    const val ADD_OR_EDIT_EXERCISE_SCREEN = "addOrEditExerciseScreen"
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
    const val ADD_OR_EDIT_EXERCISE_ROUTE = "$ADD_OR_EDIT_EXERCISE_SCREEN/{$addOrEditArgs}?$bodyPartArgs={$bodyPartArgs}?$nameExerciseArgs={$nameExerciseArgs}?$imgUrlExerciseArgs={$imgUrlExerciseArgs}?$notesExerciseArgs={$notesExerciseArgs}"
}