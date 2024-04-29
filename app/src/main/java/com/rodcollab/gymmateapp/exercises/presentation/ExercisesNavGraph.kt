package com.rodcollab.gymmateapp.exercises.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens.EXERCISES

fun NavGraphBuilder.exercisesGraph(navController: NavController,sharedViewModel: BPExercisesViewModel) {
    composable(route = GymMateScreens.MAIN_SCREEN) {
        BodyPartScreen(
            goTo = { navController.navigate(EXERCISES) },
            viewModel = sharedViewModel
        )
    }
    composable(route = EXERCISES) {
        ExercisesScreen(
            navigateUp = { navController.navigateUp() },
            sharedViewModel = sharedViewModel
        )
    }
}