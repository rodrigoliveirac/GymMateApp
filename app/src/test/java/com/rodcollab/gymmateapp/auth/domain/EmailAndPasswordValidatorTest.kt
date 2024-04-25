package com.rodcollab.gymmateapp.auth.domain

import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidator
import com.rodcollab.gymmateapp.auth.domain.usecase.EmailAndPasswordValidatorImpl
import com.rodcollab.gymmateapp.core.ResultOf
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class EmailAndPasswordValidatorTest {

    private lateinit var emailAndPasswordValidator: EmailAndPasswordValidator

    @Before
    fun initValidator() {
        emailAndPasswordValidator = EmailAndPasswordValidatorImpl()
    }

    @Test
    fun `test when email is invalid`()  {
        emailAndPasswordValidator("121.com", "123456") { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals(exceptionEmailNotValid.message, (resultOf as ResultOf.Failure).message)
        }
    }

    @Test
    fun `test when password is invalid`()  {
        emailAndPasswordValidator("rodrigo@gmail.com", "12345") { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals(exceptionPasswordNotValid.message, (resultOf as ResultOf.Failure).message)
        }
    }

    @Test
    fun `test when email is valid`()  {
        emailAndPasswordValidator("rodrigo@hotmail.com", "123456") { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Success)
        }
    }

    @Test
    fun `test when password is valid`()  {
        emailAndPasswordValidator("rodrigo@gmail.com", "123456") { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Success)
        }
    }

    @Test
    fun `test when password is valid and email not`()  {
        emailAndPasswordValidator("rodrigo@.com", "123456") { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Failure)
            TestCase.assertEquals(exceptionEmailNotValid.message, (resultOf as ResultOf.Failure).message)
        }
    }

    companion object {
        private val exceptionEmailNotValid = RuntimeException("Email is not valid")
        private val exceptionPasswordNotValid = RuntimeException("Password should have at least 6 characters")
    }
}