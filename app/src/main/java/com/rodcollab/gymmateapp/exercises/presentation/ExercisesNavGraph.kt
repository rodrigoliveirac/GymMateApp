package com.rodcollab.gymmateapp.exercises.presentation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinations
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinations.ADD_OR_EDIT_EXERCISE_ROUTE
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinations.EXERCISES_ROUTE
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.bodyPartArgs
import com.rodcollab.gymmateapp.exercises.presentation.screens.AddOrEditExerciseScreen
import com.rodcollab.gymmateapp.exercises.presentation.screens.BodyPartScreen
import com.rodcollab.gymmateapp.exercises.presentation.screens.ExerciseDetails
import com.rodcollab.gymmateapp.exercises.presentation.screens.ExercisesScreen

fun NavGraphBuilder.exercisesGraph(
    navController: NavController,
) {
    composable(
        route = GymMateScreens.MAIN_SCREEN
    ) {
        BodyPartScreen(
            goTo = { route ->
                navController.navigate(route)
            }
        )
    }
    composable(
        route = EXERCISES_ROUTE,
        arguments = listOf(navArgument(name = bodyPartArgs) { type = NavType.StringType})
    ) {
        ExercisesScreen(
            goTo = { navController.navigate(it) },
            navigateUp = {
                navController.navigateUp()
            }
        )
    }
    composable(
        route = ADD_OR_EDIT_EXERCISE_ROUTE,
        arguments = listOf(
            navArgument(
                GymMateDestinationsArgs.addOrEditArgs
            ) {
                type = NavType.StringType
            }, navArgument(GymMateDestinationsArgs.bodyPartArgs) {
                type = NavType.StringType
            }, navArgument(GymMateDestinationsArgs.exerciseIdArgs) {
                type = NavType.StringType
                nullable = true
            })
    ) {
        AddOrEditExerciseScreen(
            navigateUp = { newExercise ->
                navController.previousBackStackEntry?.savedStateHandle?.set<ExerciseExternal?>(
                    "newExercise",
                    newExercise
                )
                navController.popBackStack()
            }
        )
    }
    composable(
        route = GymMateDestinations.EXERCISE_DETAILS_ROUTE,
        arguments = listOf(navArgument(name = GymMateDestinationsArgs.exerciseIdArgs) {
            type = NavType.StringType
        })
    ) {
        val exerciseUpdated =
            navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<ExerciseExternal?>(
                "newExercise",
                null
            )?.collectAsState()

        ExerciseDetails(
            exerciseDeleted = { idDeleted ->
                navController.previousBackStackEntry?.savedStateHandle?.set<String>(
                    "idDeleted",
                    idDeleted
                )
                navController.navigateUp()
            },
            exerciseUpdated = exerciseUpdated?.value,
            navigateUp = {
                navController.navigateUp()
            }
        ) { route ->
            navController.navigate(route)
        }
    }
}