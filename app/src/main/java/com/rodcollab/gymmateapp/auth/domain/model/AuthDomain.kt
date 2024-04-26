package com.rodcollab.gymmateapp.auth.domain.model

import com.rodcollab.gymmateapp.auth.domain.usecase.UserAuth

data class AuthDomain(
    val authenticate: UserAuth
)