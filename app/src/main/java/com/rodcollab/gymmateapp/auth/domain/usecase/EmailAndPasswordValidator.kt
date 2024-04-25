package com.rodcollab.gymmateapp.auth.domain.usecase

import com.rodcollab.gymmateapp.core.ResultOf

interface EmailAndPasswordValidator {
    operator fun invoke(email:String, password: String, onResult: (ResultOf<Boolean>) -> Unit)
}