package com.rodcollab.gymmateapp.auth.domain.usecase.enums

import com.rodcollab.gymmateapp.R

sealed class SignPath(open val topBarTitle: Int, open val headlineScreen: Int, open val confirmBtnText: Int, open val externalAccount: Int) {
    data class SignIn(
        override val topBarTitle: Int = R.string.sign_in,
        override val headlineScreen: Int = R.string.auth_welcome_text,
        override val confirmBtnText: Int = R.string.auth_login_text_button,
        override val externalAccount: Int = R.string.auth_login_google_account

    ) : SignPath(topBarTitle,headlineScreen,confirmBtnText, externalAccount)
    data class SignUp(
        override val topBarTitle: Int = R.string.sign_up,
        override val headlineScreen: Int = R.string.auth_new_account_text,
        override val confirmBtnText: Int = R.string.auth_sign_up,
        override val externalAccount: Int = R.string.auth_sign_up_google_account
    ) : SignPath(topBarTitle, headlineScreen, confirmBtnText, externalAccount)

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