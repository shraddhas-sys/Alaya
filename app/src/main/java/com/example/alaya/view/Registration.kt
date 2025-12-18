package com.example.alaya.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alaya.ui.theme.AccentPurple
import com.example.alaya.ui.theme.BackgroundTan
import com.example.alaya.viewmodel.AuthViewModel

@Composable
fun RegistrationScreen(navController: NavHostController) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val authViewModel: AuthViewModel = viewModel ()

    var firstName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(BackgroundTan)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(AccentPurple)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ALAYA",
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = "Create Your Account",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 32.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FormInputField(label = "First Name", value = firstName, onValueChange = { firstName = it }, modifier = Modifier.weight(1f))
                    FormInputField(label = "Email", value = email, onValueChange = { email = it }, modifier = Modifier.weight(1f))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FormInputField(label = "Password", value = password, onValueChange = { password = it }, isPassword = true, modifier = Modifier.weight(1f))
                    FormInputField(label = "Confirm", value = confirmPassword, onValueChange = { confirmPassword = it }, isPassword = true, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    when {
                        firstName.isBlank() || email.isBlank() || password.isBlank() ->
                            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                        password != confirmPassword ->
                            Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                        password.length < 6 ->
                            Toast.makeText(context, "Password too short!", Toast.LENGTH_SHORT).show()
                        else -> {
                            authViewModel.register(firstName, email, password) { success, error ->
                                if (success) {
                                    Toast.makeText(context, "Welcome to ALAYA!", Toast.LENGTH_LONG).show()
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, error ?: "Registration failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Text("Register", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Already have an account? Login here",
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    isPassword: Boolean = false
) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}