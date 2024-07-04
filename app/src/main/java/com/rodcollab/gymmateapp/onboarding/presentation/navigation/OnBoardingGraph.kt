package com.rodcollab.gymmateapp.onboarding.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.ONBOARDING_SCREEN
import com.rodcollab.gymmateapp.onboarding.presentation.OnBoardingScreen

fun NavGraphBuilder.onboardingGraph(navController: NavController) {
    composable(route = ONBOARDING_SCREEN) {
        OnBoardingScreen()
    }
}