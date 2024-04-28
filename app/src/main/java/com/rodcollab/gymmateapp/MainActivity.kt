package com.rodcollab.gymmateapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rodcollab.gymmateapp.auth.presentation.AuthScreen
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinations.SIGNIN_ROUTE
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinations.SIGNUP_ROUTE
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens
import com.rodcollab.gymmateapp.core.ui.theme.GymMateAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymMateAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = SIGNIN_ROUTE) {
                        composable(
                            route = SIGNIN_ROUTE,
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
                            route = SIGNUP_ROUTE,
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
                }
            }
        }
    }
}