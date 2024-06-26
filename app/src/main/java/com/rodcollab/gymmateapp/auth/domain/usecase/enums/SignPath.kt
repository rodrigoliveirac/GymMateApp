package com.rodcollab.gymmateapp.auth.domain.usecase.enums

import com.rodcollab.gymmateapp.R

sealed class SignPath(open val topBarTitle: Int, open val headlineScreen: Int, open val confirmBtnText: Int) {
    data class SignIn(
        override val topBarTitle: Int = R.string.sign_in,
        override val headlineScreen: Int = R.string.auth_welcome_text,
        override val confirmBtnText: Int = R.string.auth_login_text_button
    ) : SignPath(topBarTitle,headlineScreen,confirmBtnText)
    data class SignUp(
        override val topBarTitle: Int = R.string.sign_up,
        override val headlineScreen: Int = R.string.auth_new_account_text,
        override val confirmBtnText: Int = R.string.auth_sign_up
    ) : SignPath(topBarTitle, headlineScreen, confirmBtnText)

    companion object {
        fun getSignPath(name: String) : SignPath {
            return when(name) {
                SIGN_IN -> {
                    SignIn()
                }
                else -> {
                    SignUp()
                }
            }
        }
        private const val SIGN_IN = "SIGN_IN"
    }
}