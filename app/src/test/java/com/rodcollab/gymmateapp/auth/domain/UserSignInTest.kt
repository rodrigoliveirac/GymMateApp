package com.rodcollab.gymmateapp.auth.domain

import com.rodcollab.gymmateapp.auth.FakeAuthRepository
import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidator
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidatorImpl
import com.rodcollab.gymmateapp.auth.domain.usecase.UserSignIn
import com.rodcollab.gymmateapp.auth.domain.usecase.UserSignInImpl
import com.rodcollab.gymmateapp.core.ResultOf
import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserSignInTest {

    private lateinit var userSignIn: UserSignIn
    private lateinit var authRepository: AuthRepository
    private lateinit var emailAndPasswordValidatorTest: EmailAndPasswordValidator

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        emailAndPasswordValidatorTest = EmailAndPasswordValidatorImpl()
        userSignIn = UserSignInImpl(authRepository,emailAndPasswordValidatorTest)
    }

    @Test
    fun `test User SignIn business logic when is Failure cause the user is not registered`() = runTest {
        val email = "rod@gmail.com"
        val password = "123456"
        userSignIn(email = email,password = password) { resultOf ->
            assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals("User not found", (resultOf as ResultOf.Failure).message)
        }
    }
    @Test
    fun `test User SignIn business logic when is Success`() = runTest {
        val email = "shadow@gmail.com"
        val password = "123456"
        val userExpected = User(uuid = 0.toString(),email = "shadow@gmail.com", password = "123456", username = "shadow")
        userSignIn(email = email,password = password) { resultOf ->
            assertTrue(resultOf is ResultOf.Success)
            TestCase.assertEquals(ResultOf.Success(userExpected), resultOf)
        }
    }

}