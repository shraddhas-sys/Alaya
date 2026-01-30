package com.example.alaya.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alaya.R
import com.example.alaya.viewmodel.AuthViewModel

@Composable
fun LoginUI(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // Purple Mix Gradient based on your UI reference
            .background(Brush.verticalGradient(listOf(Color(0xFF1F1C2C), Color(0xFF5D4B7A), Color(0xFF928DAB))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome Back!!",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Updated Email Input with Icon
            TransparentInputBox(
                value = email,
                hint = "Email",
                icon = Icons.Default.Email,
                onChange = { email = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Updated Password Input with Icon and Masking
            TransparentInputBox(
                value = password,
                hint = "Password",
                icon = Icons.Default.Lock,
                isPasswordField = true,
                onChange = { password = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "Forgot Password?",
                    color = Color.White.copy(0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { navController.navigate("forgot") }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Purple "Log In" Button
            Button(
                onClick = {
                    authViewModel.login(
                        email = email,
                        pass = password,
                        onSuccess = {
                            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                            navController.navigate("dashboard") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onError = { errorMsg ->
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C4DB9))
            ) {
                Text("Log In", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(25.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(0.2f))
                Text("  OR  ", color = Color.White.copy(0.6f), fontSize = 12.sp)
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(0.2f))
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Google Sign-In Surface
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clickable {
                        Toast.makeText(context, "Google Sign In Clicked", Toast.LENGTH_SHORT).show()
                    },
                shape = RoundedCornerShape(30.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_8), // Ensure this exists in res/drawable
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sign in with Google",
                        color = Color(0xFF1F1C2C),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row {
                Text("Don't have an account? ", color = Color.White.copy(0.7f))
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("register") }
                )
            }
        }
    }
}

@Composable
fun TransparentInputBox(
    value: String,
    hint: String,
    icon: ImageVector,
    isPasswordField: Boolean = false,
    onChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(Color.White.copy(0.15f), RoundedCornerShape(15.dp)) // Semi-transparent box
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(0.7f),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Text(text = hint, color = Color.White.copy(0.4f), fontSize = 16.sp)
                }
                BasicTextField(
                    value = value,
                    onValueChange = onChange,
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    // Hide password characters if it's a password field
                    visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None
                )
            }
        }
    }
}