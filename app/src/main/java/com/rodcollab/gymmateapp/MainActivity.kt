package com.rodcollab.gymmateapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinations.SIGNIN_ROUTE
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens.MAIN_SCREEN
import com.rodcollab.gymmateapp.auth.presentation.navigation.authGraph
import com.rodcollab.gymmateapp.core.ui.theme.GymMateAppTheme
import com.rodcollab.gymmateapp.exercises.presentation.BPExercisesViewModel
import com.rodcollab.gymmateapp.exercises.presentation.exercisesGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userIsLogged = (this.application as GymMateApp).auth.currentUser != null
        val sharedViewModel by viewModels<BPExercisesViewModel>()

        setContent {
            GymMateAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if (userIsLogged) MAIN_SCREEN else SIGNIN_ROUTE
                    ) {
                        authGraph(navController)
                        exercisesGraph(navController,sharedViewModel)
                    }
                }
            }
        }
    }
}