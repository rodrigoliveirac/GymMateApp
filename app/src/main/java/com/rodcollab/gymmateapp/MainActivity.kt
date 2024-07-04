package com.rodcollab.gymmateapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rodcollab.gymmateapp.core.ui.theme.GymMateAppTheme
import com.rodcollab.gymmateapp.auth.presentation.navigation.authGraph
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinations.SIGNIN_ROUTE
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.ONBOARDING_SCREEN
import com.rodcollab.gymmateapp.onboarding.presentation.navigation.onboardingGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userIsLogged = (this.application as GymMateApp).auth.currentUser != null

        setContent {
            GymMateAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = if(userIsLogged) ONBOARDING_SCREEN else SIGNIN_ROUTE) {
                    authGraph(navController = navController)
                    onboardingGraph(navController = navController)
                }
            }
        }
    }
}