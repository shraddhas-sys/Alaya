package com.example.alaya

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthAppInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()
    // Login Ui test

    @Test
    fun testEmptyLogin_showsErrorMessage() {
        composeRule.onNodeWithTag("loginButton").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Empty fields").assertIsDisplayed()
    }

    @Test
    fun testSuccessfulLogin_navigation() {
        // input field part
        composeRule.onNodeWithTag("emailField").performTextInput("test@gmail.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("password123")
        composeRule.onNodeWithTag("loginButton").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Welcome Home").assertExists()
    }

    // Registration Ui tests

    @Test
    fun testRegisterForm_validationError() {
        composeRule.onNodeWithTag("nameField").performTextInput("Meow User")
        composeRule.onNodeWithTag("registerButton").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Fill all fields").assertIsDisplayed()
    }

    @Test
    fun testNavigation_toForgotPassword() {
        // forgot password link part
        composeRule.onNodeWithTag("forgotPasswordLink").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Enter email").assertIsDisplayed()
    }
}