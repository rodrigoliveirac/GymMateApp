package com.rodcollab.gymmateapp.auth

import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.core.ResultOf
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepository

    @Before
    fun initRepository() {
        authRepository = FakeAuthRepository()
    }
    @Test
    fun `test when the ResultOf is Failure`() = runTest {

        val email = "rodrigo@gmail.com"
        val password = "12345"

        val exception = NullPointerException()

        val resultOfFailure: ResultOf<User> = ResultOf.Failure(message = exception.message, throwable = exception.cause)

        authRepository.signInWithEmailAndPassword(email,password, onResult = {
            val actual: ResultOf<User> = it
            val expected: ResultOf<User> = resultOfFailure
            assertThat(actual,equalTo(expected))
        })
    }

    @Test
    fun `test when the ResultOf is Success`() = runTest {
        val email = "shadow@gmail.com"
        val password = "1112"

        val resultOfSuccess: ResultOf<User> = ResultOf.Success(User(email = email, password = password, username = "shadow"))

        authRepository.signInWithEmailAndPassword(email, password, onResult = { resultOf ->
            val actual: ResultOf<User> = resultOf
            val expected: ResultOf<User> = resultOfSuccess
            assertThat(actual,equalTo(expected))
        })
    }
    @Test
    fun `test when user is not found`() = runTest {
        val email = "unknown@gmail.com"
        val password = "password"

        authRepository.signInWithEmailAndPassword(email, password) { resultOf ->
            assertTrue(resultOf is ResultOf.Failure)
            assertEquals("User not found", (resultOf as ResultOf.Failure).message)
        }
    }
    @Test
    fun `Test when user registration is unsuccessful because the email already exists`() = runTest {

        val email = "shadow@gmail.com"
        val password = "password"

        authRepository.createUserWithEmailAndPassword(email, password) { resultOf ->
            assertTrue(resultOf is ResultOf.Failure)
            assertEquals("User already Exist", (resultOf as ResultOf.Failure).message)
        }
    }
    @Test
    fun `Test when user registration is successful`() = runTest {

        val email = "rodo@gmail.com"
        val password = "password"

        val fakeAuthRepository = authRepository as FakeAuthRepository
        val uuid = (fakeAuthRepository.users.last().uuid?.toInt()?.plus(1)).toString()
        val newUserExpected = User(
            uuid  = uuid,
            username = email.split("@")[0],
            email = email,
            password = password
        )

        authRepository.createUserWithEmailAndPassword(email, password) { resultOf ->
            assertTrue(resultOf is ResultOf.Success)
            assertEquals(ResultOf.Success(newUserExpected), resultOf)
        }
    }

}