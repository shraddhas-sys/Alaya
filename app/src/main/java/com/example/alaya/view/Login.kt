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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
            .background(Brush.verticalGradient(listOf(Color(0xFF1F1C2C), Color(0xFF928DAB))))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome Back!!", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(30.dp))

            // These now work because the function is defined below
            TransparentInputBox(value = email, hint = "Email") { email = it }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentInputBox(value = password, hint = "Password") { password = it }

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    "Forgot Password?",
                    color = Color.White.copy(0.8f),
                    modifier = Modifier.clickable { navController.navigate("forgot") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E44AD))
            ) {
                Text("Log In", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(0.3f))
                Text("  OR  ", color = Color.White.copy(0.6f), fontSize = 12.sp)
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(0.3f))
            }

            Spacer(modifier = Modifier.height(20.dp))
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
                        painter = painterResource(id = R.drawable.img_8),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Sign in with Google",
                        color = Color(0xFF1F1C2C),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row {
                Text("Don't have an account? ", color = Color.White.copy(0.7f))
                Text(
                    "Sign Up",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("register") }
                )
            }
        }
    }
}
@Composable
fun TransparentInputBox(value: String, hint: String, onChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White.copy(0.2f), RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            Text(hint, color = Color.White.copy(0.5f))
        }
        BasicTextField(
            value = value,
            onValueChange = onChange,
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}