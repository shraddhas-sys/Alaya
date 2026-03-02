package com.example.alaya.viewmodel

import com.example.alaya.repository.AuthRepoImpl
import com.google.firebase.auth.FirebaseUser
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.MockedConstruction
import org.mockito.Mockito.mockConstruction
import org.mockito.kotlin.*

class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private lateinit var mockRepo: AuthRepoImpl

    @Before
    fun setup() {
        val construction: MockedConstruction<AuthRepoImpl> = mockConstruction(AuthRepoImpl::class.java) { mock, _ ->
            mockRepo = mock
        }

       // view model initialization
        viewModel = AuthViewModel()

        construction.close()
    }

    @Test
    fun `login with empty fields calls onError`() {
        var errorMsg = ""
        viewModel.login("", "", onSuccess = {}, onError = { errorMsg = it })

        assertEquals("Empty fields", errorMsg)
        verifyNoInteractions(mockRepo)
    }

    @Test
    fun `login success scenario`() {
        val email = "test@test.com"
        val pass = "pass123"
        var successTriggered = false

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(true, null)
            null
        }.whenever(mockRepo).login(eq(email), eq(pass), any())

        viewModel.login(email, pass, onSuccess = { successTriggered = true }, onError = {})

        assert(successTriggered)
    }

    @Test
    fun `register failure scenario`() {
        val name = "Test User"
        val email = "test@test.com"
        val pass = "password"
        val repoError = "Email already exists"

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(false, repoError)
            null
        }.whenever(mockRepo).register(eq(email), eq(pass), any())

        viewModel.register(name, email, pass) { success, error ->
            assertEquals(false, success)
            assertEquals(repoError, error)
        }
    }

    @Test
    fun `forgotPassword success scenario`() {
        val email = "reset@test.com"

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(1)
            callback(true, null)
            null
        }.whenever(mockRepo).forgotPassword(eq(email), any())

        var result = false
        viewModel.forgotPassword(email) { success, _ ->
            result = success
        }

        assert(result)
        verify(mockRepo).forgotPassword(eq(email), any())
    }
}