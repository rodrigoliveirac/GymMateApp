package com.rodcollab.gymmateapp.auth.domain.usecase

import com.rodcollab.gymmateapp.core.ResultOf

interface EmailAndPasswordValidator {
      suspend operator fun invoke(email:String, password: String, repeatPassword: String?, onResult: suspend (ResultOf<Boolean>) -> Unit)
}