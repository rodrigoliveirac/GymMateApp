package com.rodcollab.gymmateapp.exercises.presentation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinations
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinations.ADD_OR_EDIT_EXERCISE_ROUTE
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens.EXERCISES
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.exercises.presentation.screens.AddOrEditExerciseScreen
import com.rodcollab.gymmateapp.exercises.presentation.screens.BodyPartScreen
import com.rodcollab.gymmateapp.exercises.presentation.screens.ExerciseDetails
import com.rodcollab.gymmateapp.exercises.presentation.screens.ExercisesScreen

fun NavGraphBuilder.exercisesGraph(
    navController: NavController,
    sharedViewModel: BPExercisesViewModel
) {
    composable(
        route = GymMateScreens.MAIN_SCREEN
    ) {
        BodyPartScreen(
            goTo = { route ->
                navController.navigate(route)
            },
            viewModel = sharedViewModel
        )
    }
    composable(route = EXERCISES) {
        val newExercise =
            navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<ExerciseExternal?>(
                "newExercise",
                null
            )?.collectAsState()
        ExercisesScreen(
            newExercise = newExercise?.value,
            goTo = { navController.navigate(it) },
            navigateUp = {
                navController.navigateUp()
            },
            sharedViewModel = sharedViewModel
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
                nullable = true
            }, navArgument(GymMateDestinationsArgs.nameExerciseArgs) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }, navArgument(GymMateDestinationsArgs.imgUrlExerciseArgs) {
                type = NavType.StringType
                nullable = true
            }, navArgument(GymMateDestinationsArgs.notesExerciseArgs) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
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
        ExerciseDetails(
            navigateUp = {
                navController.navigateUp()
            }
        )
    }
}