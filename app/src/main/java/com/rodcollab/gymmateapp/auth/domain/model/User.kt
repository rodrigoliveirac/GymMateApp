package com.rodcollab.gymmateapp.auth.domain.model

data class User(
    val uuid: String? = null,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null
)
