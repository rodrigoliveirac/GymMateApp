package com.rodcollab.gymmateapp.auth.domain

import com.rodcollab.gymmateapp.auth.FakeAuthRepository
import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidator
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidatorImpl
import com.rodcollab.gymmateapp.auth.domain.usecase.SignPath
import com.rodcollab.gymmateapp.auth.domain.usecase.UserAuth
import com.rodcollab.gymmateapp.auth.domain.usecase.UserAuthImpl
import com.rodcollab.gymmateapp.core.ResultOf
import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserAuthTest {

    private lateinit var userSignIn: UserAuth
    private lateinit var authRepository: AuthRepository
    private lateinit var emailAndPasswordValidatorTest: EmailAndPasswordValidator

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        emailAndPasswordValidatorTest = EmailAndPasswordValidatorImpl()
        userSignIn = UserAuthImpl(authRepository,emailAndPasswordValidatorTest)
    }

    @Test
    fun `test User SignIn business logic when is Failure cause the user is not registered`() = runTest {
        val email = "rod@gmail.com"
        val password = "123456"
        userSignIn(email = email,password = password, signPath = SignPath.SIGN_IN) { resultOf ->
            assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals("User not found", (resultOf as ResultOf.Failure).message)
        }
    }
    @Test
    fun `test User SignIn business logic when is Success`() = runTest {
        val email = "shadow@gmail.com"
        val password = "123456"
        val userExpected = User(uuid = 0.toString(),email = "shadow@gmail.com", password = "123456", username = "shadow")
        userSignIn(email = email,password = password, signPath = SignPath.SIGN_IN) { resultOf ->
            assertTrue(resultOf is ResultOf.Success)
            TestCase.assertEquals(ResultOf.Success(userExpected), resultOf)
        }
    }

    @Test
    fun `test User SignUp business logic when is Failure cause the user email already exist`() = runTest {
        val email = "shadow@gmail.com"
        val password = "123456"
        userSignIn(email = email,password = password, signPath = SignPath.SIGN_UP) { resultOf ->
            assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals("User already Exist", (resultOf as ResultOf.Failure).message)
        }
    }
    @Test
    fun `test User SignUp business logic when is Success`() = runTest {
        val email = "shalow@gmail.com"
        val password = "123456"
        val fakeAuthRepository = authRepository as FakeAuthRepository
        val uuid = (fakeAuthRepository.users.last().uuid?.toInt()?.plus(1)).toString()
        val newUserExpected = User(
            uuid  = uuid,
            username = email.split("@")[0],
            email = email,
            password = password
        )
        userSignIn(email = email,password = password, signPath = SignPath.SIGN_UP) { resultOf ->
            assertTrue(resultOf is ResultOf.Success)
            TestCase.assertEquals(ResultOf.Success(newUserExpected), resultOf)
        }
    }

}