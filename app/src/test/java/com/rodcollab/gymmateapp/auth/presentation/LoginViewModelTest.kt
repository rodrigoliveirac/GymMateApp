package com.rodcollab.gymmateapp.auth.presentation

import com.rodcollab.gymmateapp.auth.FakeAuthRepository
import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.domain.model.AuthDomain
import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidator
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidatorImpl
import com.rodcollab.gymmateapp.auth.domain.usecase.UserAuth
import com.rodcollab.gymmateapp.auth.domain.usecase.UserAuthImpl
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.auth.presentation.intent.AuthUiAction
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
    private lateinit var fakeSavedStateHandle:FakeSavedStateHandle

    @Before
    fun setup() {
        authRepository = FakeAuthRepository()
        emailAndPasswordValidatorTest = EmailAndPasswordValidatorImpl()
        userSignIn = UserAuthImpl(authRepository,emailAndPasswordValidatorTest)
        authDomain = AuthDomain(authenticate = UserAuthImpl(authRepository,emailAndPasswordValidatorTest))
        fakeSavedStateHandle = FakeSavedStateHandle()
    }

    @Test
    fun `test when the user is changing the email value`() = runTest {

        fakeSavedStateHandle[SIGN_PATH] = signInPath

        val model = AuthUiModel(UnconfinedTestDispatcher(testScheduler),authDomain, fakeSavedStateHandle)

        val states = mutableListOf<AuthUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }

        val initialValue = ""
        val digit1 = "r"
        val digit2 = "o"
        val digit3 = "d"

        model.onAuthUiAction(AuthUiAction.OnEmailValueChange(email = digit1))
        model.onAuthUiAction(AuthUiAction.OnEmailValueChange(email = digit2))
        model.onAuthUiAction(AuthUiAction.OnEmailValueChange(email = digit3))

        assertThat(AuthUiState(email = initialValue), equalTo(states[0]))
        assertThat(AuthUiState(email = states[0].email.plus(digit1)), equalTo(states[1]))
        assertThat(AuthUiState(email = states[1].email.plus(digit2)), equalTo(states[2]))
        assertThat(AuthUiState(email = states[2].email.plus(digit3)), equalTo(states[3]))

        job.cancel()
    }

    @Test
    fun `when the user want to show password`() = runTest {

        fakeSavedStateHandle[SIGN_PATH] = signInPath

        val model = AuthUiModel(UnconfinedTestDispatcher(testScheduler),authDomain, fakeSavedStateHandle)

        val states = mutableListOf<AuthUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }

        model.onAuthUiAction(AuthUiAction.OnShowPasswordClick)

        assertThat(true, equalTo(states[1].showPassword))

        job.cancel()
    }

    @Test
    fun `when the user want to login with wrong data`() = runTest {

        fakeSavedStateHandle[SIGN_PATH] = signInPath

        val model = AuthUiModel(UnconfinedTestDispatcher(testScheduler),authDomain,fakeSavedStateHandle)

        val states = mutableListOf<AuthUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }
        val email = "rodrigo@gmail.com"
        val password = "123456"
        model.onAuthUiAction(AuthUiAction.OnEmailValueChange(email = email))
        model.onAuthUiAction(AuthUiAction.OnPasswordValueChange(password = password))
        model.onAuthUiAction(AuthUiAction.OnConfirmClick)

        assertThat(email, equalTo(states[1].email))
        assertThat(password, equalTo(states[2].password))
        assertThat(ResultOf.Idle, equalTo(states[3].resultOf))
        TestCase.assertTrue(states[4].resultOf is ResultOf.Failure)
        TestCase.assertEquals("User not found", (states[4].resultOf as ResultOf.Failure).message)

        job.cancel()
    }

    @Test
    fun `when the user want to login with right data`() = runTest {

        fakeSavedStateHandle["SIGN_PATH"] = signInPath

        val model = AuthUiModel(UnconfinedTestDispatcher(testScheduler),authDomain, fakeSavedStateHandle)

        val states = mutableListOf<AuthUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }
        val email = "shadow@gmail.com"
        val password = "123456"
        val username = "shadow"
        model.onAuthUiAction(AuthUiAction.OnEmailValueChange(email = email))
        model.onAuthUiAction(AuthUiAction.OnPasswordValueChange(password = password))
        model.onAuthUiAction(AuthUiAction.OnConfirmClick)

        assertThat(email, equalTo(states[1].email))
        assertThat(password, equalTo(states[2].password))
        assertThat(ResultOf.Idle, equalTo(states[3].resultOf))

        TestCase.assertTrue(states[4].resultOf is ResultOf.Success)
        TestCase.assertEquals(email, (states[4].resultOf as ResultOf.Success).value.email)
        TestCase.assertEquals(password, (states[4].resultOf as ResultOf.Success).value.password)
        TestCase.assertEquals(username, (states[4].resultOf as ResultOf.Success).value.username)

        job.cancel()
    }

    @Test
    fun `when the user want to register with data that already exist`() = runTest {

        fakeSavedStateHandle[SIGN_PATH] = signUpPath

        val model = AuthUiModel(UnconfinedTestDispatcher(testScheduler),authDomain,fakeSavedStateHandle)

        val states = mutableListOf<AuthUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }
        val email = "shadow@gmail.com"
        val password = "123456"
        model.onAuthUiAction(AuthUiAction.OnEmailValueChange(email = email))
        model.onAuthUiAction(AuthUiAction.OnPasswordValueChange(password = password))
        model.onAuthUiAction(AuthUiAction.OnConfirmClick)

        assertThat(email, equalTo(states[1].email))
        assertThat(password, equalTo(states[2].password))
        assertThat(ResultOf.Idle, equalTo(states[3].resultOf))
        TestCase.assertTrue(states[4].resultOf is ResultOf.Failure)
        TestCase.assertEquals("User already Exist", (states[4].resultOf as ResultOf.Failure).message)

        job.cancel()
    }

    @Test
    fun `when the user want to register and then is successfully`() = runTest {

        fakeSavedStateHandle[SIGN_PATH] = signUpPath

        val model = AuthUiModel(UnconfinedTestDispatcher(testScheduler),authDomain,fakeSavedStateHandle)

        val states = mutableListOf<AuthUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            model.uiState.toList(states)
        }
        val email = "rodrigo@gmail.com"
        val password = "123456"
        model.onAuthUiAction(AuthUiAction.OnEmailValueChange(email = email))
        model.onAuthUiAction(AuthUiAction.OnPasswordValueChange(password = password))
        model.onAuthUiAction(AuthUiAction.OnConfirmClick)

        val fakeAuthRepository = authRepository as FakeAuthRepository

        val uuid = (fakeAuthRepository.users.last().uuid?.toInt()?.plus(1)).toString()
        val newUserExpected = User(
            uuid  = uuid,
            username = email.split("@")[0],
            email = email,
            password = password
        )

        assertThat(email, equalTo(states[1].email))
        assertThat(password, equalTo(states[2].password))
        assertThat(ResultOf.Idle, equalTo(states[3].resultOf))
        TestCase.assertTrue(states[4].resultOf is ResultOf.Success)
        TestCase.assertEquals(newUserExpected, (states[4].resultOf as ResultOf.Success).value)

        job.cancel()
    }

    companion object {
        private val signInPath = SignPath.SIGN_IN.name
        private val signUpPath = SignPath.SIGN_UP.name
        private val SIGN_PATH = "SIGN_PATH"
    }

}