package com.rodcollab.gymmateapp.auth.domain.usecase

import com.rodcollab.gymmateapp.core.ResultOf
import java.util.regex.Pattern

class EmailAndPasswordValidatorImpl : EmailAndPasswordValidator {
    override suspend fun invoke(email: String, password: String, repeatPassword: String?, onResult: suspend (ResultOf<Boolean>) -> Unit) {
        try {
            checkIfEmailIsValid(email)
            checkIfPasswordIsValid(password)
            repeatPassword?.let {
                checkIfPasswordsAreTheSame(password = password, repeatPassword = it)
            }
            onResult(ResultOf.Success(true))
        } catch (e: Exception) {
            onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
        }
    }

    private fun checkIfEmailIsValid(email: String) {
        val pattern = Pattern.compile("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")
        val matcher = pattern.matcher(email)
        if(!matcher.matches()) {
            throw RuntimeException("Email is not valid")
        }
    }
    private fun checkIfPasswordIsValid(password: String) {
        val isValid = password.length >= 6
        if(!isValid) {
            throw RuntimeException("Password should have at least 6 characters")
        }
    }

    private fun checkIfPasswordsAreTheSame(password: String, repeatPassword: String) {
        if(repeatPassword != password) {
            throw RuntimeException("The passwords should be the same")
        }
    }
}