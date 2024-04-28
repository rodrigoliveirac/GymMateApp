package com.rodcollab.gymmateapp.auth.domain.usecase

import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.core.ResultOf

interface UserAuth {
    suspend operator fun invoke(email:String, password: String, repeatPassword: String?, signPath: SignPath, onResult: (ResultOf<User>) -> Unit)
}