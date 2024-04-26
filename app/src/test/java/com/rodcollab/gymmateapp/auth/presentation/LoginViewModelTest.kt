package com.rodcollab.gymmateapp.auth.presentation

import com.rodcollab.gymmateapp.auth.FakeAuthRepository
import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.domain.model.AuthDomain
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidator
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidatorImpl
import com.rodcollab.gymmateapp.auth.domain.usecase.UserAuth
import com.rodcollab.gymmateapp.auth.domain.usecase.UserAuthImpl
import com.rodcollab.gymmateapp.auth.presentation.intent.LoginUiAction
import com.rodcollab.gymmateapp.core.ResultOf
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var authDomain: AuthDomain
    private lateinit var userSignIn: UserAuth
    private lateinit var authRepository: AuthRepository
    private lateinit var emailAndPasswordValidatorTest: EmailAndPasswordValidator

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        emailAndPasswordValidatorTest = EmailAndPasswordValidatorImpl()
        userSignIn = UserAuthImpl(authRepository,emailAndPasswordValidatorTest)
        authDomain = AuthDomain(authenticate = UserAuthImpl(authRepository,emailAndPasswordValidatorTest))
    }

    @Test
    fun `test when the user is changing the email value`() = runTest {

        val model = LoginUiModel(UnconfinedTestDispatcher(testScheduler),authDomain)

        val states = mutableListOf<LoginUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }

        val initialValue = ""
        val digit1 = "r"
        val digit2 = "o"
        val digit3 = "d"

        model.onLoginUiAction(LoginUiAction.OnEmailValueChange(email = digit1))
        model.onLoginUiAction(LoginUiAction.OnEmailValueChange(email = digit2))
        model.onLoginUiAction(LoginUiAction.OnEmailValueChange(email = digit3))

        assertThat(LoginUiState(email = initialValue), equalTo(states[0]))
        assertThat(LoginUiState(email = states[0].email.plus(digit1)), equalTo(states[1]))
        assertThat(LoginUiState(email = states[1].email.plus(digit2)), equalTo(states[2]))
        assertThat(LoginUiState(email = states[2].email.plus(digit3)), equalTo(states[3]))

        job.cancel()
    }

    @Test
    fun `when the user want to show password`() = runTest {

        val model = LoginUiModel(UnconfinedTestDispatcher(testScheduler),authDomain)

        val states = mutableListOf<LoginUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }

        model.onLoginUiAction(LoginUiAction.OnShowPasswordClick)

        assertThat(true, equalTo(states[1].showPassword))

        job.cancel()
    }

    @Test
    fun `when the user want to login with wrong data`() = runTest {

        val model = LoginUiModel(UnconfinedTestDispatcher(testScheduler),authDomain)

        val states = mutableListOf<LoginUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }
        val email = "rodrigo@gmail.com"
        val password = "123456"
        model.onLoginUiAction(LoginUiAction.OnEmailValueChange(email = email))
        model.onLoginUiAction(LoginUiAction.OnPasswordValueChange(password = password))
        model.onLoginUiAction(LoginUiAction.OnLoginClick)

        assertThat(email, equalTo(states[1].email))
        assertThat(password, equalTo(states[2].password))
        assertThat(ResultOf.Idle, equalTo(states[3].resultOf))
        TestCase.assertTrue(states[4].resultOf is ResultOf.Failure)
        TestCase.assertEquals("User not found", (states[4].resultOf as ResultOf.Failure).message)

        job.cancel()
    }

    @Test
    fun `when the user want to login with right data`() = runTest {

        val model = LoginUiModel(UnconfinedTestDispatcher(testScheduler),authDomain)

        val states = mutableListOf<LoginUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }
        val email = "shadow@gmail.com"
        val password = "123456"
        val username = "shadow"
        model.onLoginUiAction(LoginUiAction.OnEmailValueChange(email = email))
        model.onLoginUiAction(LoginUiAction.OnPasswordValueChange(password = password))
        model.onLoginUiAction(LoginUiAction.OnLoginClick)

        assertThat(email, equalTo(states[1].email))
        assertThat(password, equalTo(states[2].password))
        assertThat(ResultOf.Idle, equalTo(states[3].resultOf))

        TestCase.assertTrue(states[4].resultOf is ResultOf.Success)
        TestCase.assertEquals(email, (states[4].resultOf as ResultOf.Success).value.email)
        TestCase.assertEquals(password, (states[4].resultOf as ResultOf.Success).value.password)
        TestCase.assertEquals(username, (states[4].resultOf as ResultOf.Success).value.username)

        job.cancel()
    }
}