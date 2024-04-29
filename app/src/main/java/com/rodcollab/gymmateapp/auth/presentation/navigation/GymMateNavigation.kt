package com.rodcollab.gymmateapp.auth.presentation.navigation

import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.SIGN_PATH
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.emailArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.passwordArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens.AUTH_SCREEN

/**
 * Screens used in [GymMateDestinations]
 */
internal object GymMateScreens {
    const val MAIN_SCREEN = "main"
    const val AUTH_SCREEN = "authentication"
    const val EXERCISES = "exercises"
}

/**
 * Arguments used in [GymMateDestinations] routes
 */
object GymMateDestinationsArgs {
    const val SIGN_PATH = "signPath"
    const val emailArgs = "email"
    const val passwordArgs = "password"
}

/**
 * Destinations used in the [MainActivity]
 */
object GymMateDestinations {
    const val SIGNUP_ROUTE = "$AUTH_SCREEN/{$SIGN_PATH}"
    const val SIGNIN_ROUTE = "$AUTH_SCREEN/{$SIGN_PATH}?$emailArgs={$emailArgs}?$passwordArgs={$passwordArgs}"
}