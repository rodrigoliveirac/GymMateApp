package com.rodcollab.gymmateapp.auth.domain.di

import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.domain.model.AuthDomain
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidatorImpl
import com.rodcollab.gymmateapp.auth.domain.usecase.UserAuthImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object AuthDomainModule {

    @ViewModelScoped
    @Provides
    fun providesAuthDomain(
        authRepository: AuthRepository,
    ): AuthDomain {
        return AuthDomain(
            authenticate = UserAuthImpl(
                authRepository = authRepository,
                emailAndPasswordValidator = EmailAndPasswordValidatorImpl()
            )
        )
    }
}