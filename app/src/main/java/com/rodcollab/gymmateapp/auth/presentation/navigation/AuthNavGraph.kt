package com.rodcollab.gymmateapp.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rodcollab.gymmateapp.auth.presentation.AuthScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    composable(
        route = GymMateDestinations.SIGNIN_ROUTE,
        arguments = listOf(navArgument(GymMateDestinationsArgs.SIGN_PATH) {
            type = NavType.StringType
        }, navArgument(GymMateDestinationsArgs.emailArgs) {
            type = NavType.StringType
            nullable = true
        }, navArgument(GymMateDestinationsArgs.passwordArgs) {
            type = NavType.StringType
            nullable = true
        })
    ) {
        AuthScreen { route ->
            route?.let {
                navController.navigate(it) {
                    if (route == GymMateScreens.MAIN_SCREEN) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }
    composable(
        route = GymMateDestinations.SIGNUP_ROUTE,
        arguments = listOf(navArgument(GymMateDestinationsArgs.SIGN_PATH) {
            type = NavType.StringType
        })
    ) {
        AuthScreen { route ->
            route?.let {
                navController.navigate(it) {
                    popUpTo(navController.previousBackStackEntry?.destination?.route.toString()) {
                        inclusive = true
                    }
                }
            } ?: run {
                navController.navigateUp()
            }
        }
    }
}