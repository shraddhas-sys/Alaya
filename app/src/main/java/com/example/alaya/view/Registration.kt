package com.example.alaya.view

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alaya.ui.theme.AccentPurple
import com.example.alaya.ui.theme.AlayaPurplePrimary
import com.example.alaya.ui.theme.AlayaTextMuted
import com.example.alaya.ui.theme.BackgroundTan
import com.example.alaya.view.ui.theme.AlayaCreamBg
import com.example.alaya.view.ui.theme.AlayaPurpleLight
import com.example.alaya.view.ui.theme.AlayaWhite
import com.example.alaya.view.ui.theme.DeepPurple
import com.example.alaya.view.ui.theme.LightBlue
import com.example.alaya.viewmodel.AuthViewModel
import androidx.compose.ui.graphics.Path


@Composable
fun RegistrationScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
        val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val deepLavender = Color(0xFF4B307A)
    val midLavender = Color(0xFF7B5CAB)
    val lightLavenderBg = Color(0xFFF3E5F5)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(deepLavender, midLavender)))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(color = Color.White.copy(alpha = 0.12f), radius = 350f, center = center.copy(x = 100f, y = 150f))
            drawCircle(color = Color.White.copy(alpha = 0.08f), radius = 220f, center = center.copy(x = 900f, y = 450f))
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(100.dp))

            Column(modifier = Modifier.padding(horizontal = 35.dp)) {
                Text(
                    "Create Your\nAccount",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    lineHeight = 42.sp
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                color = lightLavenderBg
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 30.dp, vertical = 35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ModernInput(label = "Full Name", value = fullName, onValueChange = { fullName = it })
                    ModernInput(label = "Email Address", value = email, onValueChange = { email = it })
                    ModernInput(label = "Password", value = password, onValueChange = { password = it }, isPass = true)
                    ModernInput(label = "Confirm Password", value = rePassword, onValueChange = { rePassword = it }, isPass = true)

                    Spacer(modifier = Modifier.height(45.dp))

                    Button(
                        onClick = {
                            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                            } else if (password != rePassword) {
                                Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                            } else {
                                isLoading = true
                                authViewModel.register(fullName, email, password) { success, message ->
                                    isLoading = false
                                    if (success) {
                                        Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("login") {
                                            popUpTo("registration") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(58.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        enabled = !isLoading
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(listOf(deepLavender, midLavender)),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("SIGN UP", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(35.dp))
                    Row(
                        modifier = Modifier.padding(bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Already have an account? ", color = Color.Gray, fontSize = 14.sp)
                        Text(
                            "Sign In",
                            color = deepLavender,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { navController.navigate("login") }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPass: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B307A),
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 2.dp, bottom = 6.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF7B5CAB),
                unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.6f),
                cursorColor = Color(0xFF4B307A)
            ),
            visualTransformation = if (isPass) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            trailingIcon = {
                val icon = if (isPass) Icons.Default.Lock else Icons.Default.CheckCircle
                Icon(icon, null, tint = Color.Gray.copy(0.7f), modifier = Modifier.size(20.dp))
            }
        )
    }
}
