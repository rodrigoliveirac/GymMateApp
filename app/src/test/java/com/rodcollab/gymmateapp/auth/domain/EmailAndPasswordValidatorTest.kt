package com.rodcollab.gymmateapp.auth.domain

import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidator
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidatorImpl
import com.rodcollab.gymmateapp.core.ResultOf
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class EmailAndPasswordValidatorTest {

    private lateinit var emailAndPasswordValidator: EmailAndPasswordValidator

    @Before
    fun initValidator() {
        emailAndPasswordValidator = EmailAndPasswordValidatorImpl()
    }

    @Test
    fun `test when email is invalid`() = runTest {
        emailAndPasswordValidator("121.com", "123456",null) { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals(exceptionEmailNotValid.message, (resultOf as ResultOf.Failure).message)
        }
    }

    @Test
    fun `test when password is invalid`() = runTest  {
        emailAndPasswordValidator("rodrigo@gmail.com", "12345", null) { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals(exceptionPasswordNotValid.message, (resultOf as ResultOf.Failure).message)
        }
    }

    @Test
    fun `test when email is valid`() = runTest {
        emailAndPasswordValidator("rodrigo@hotmail.com", "123456", null) { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Success)
        }
    }

    @Test
    fun `test when password is valid`() = runTest {
        emailAndPasswordValidator("rodrigo@gmail.com", "123456", null) { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Success)
        }
    }

    @Test
    fun `test when password is valid and email not`() = runTest {
        emailAndPasswordValidator("rodrigo@.com", "123456", null) { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals(exceptionEmailNotValid.message, (resultOf as ResultOf.Failure).message)
        }
    }
    @Test
    fun `test when passwords are not the same`() = runTest {
        emailAndPasswordValidator("rodrigo@gmail.com", "123456", "123467") { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals(exceptionPasswordAreNotTheSame.message, (resultOf as ResultOf.Failure).message)
        }
    }
    @Test
    fun `test when passwords are the same`() = runTest {
        emailAndPasswordValidator("rodrigo@gmail.com", "123456", "123456") { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Success)
        }
    }

    companion object {
        private val exceptionEmailNotValid = RuntimeException("Email is not valid")
        private val exceptionPasswordNotValid = RuntimeException("Password should have at least 6 characters")
        private val exceptionPasswordAreNotTheSame = RuntimeException("The passwords should be the same")
    }
}