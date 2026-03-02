package com.example.alaya.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alaya.ui.theme.AlayaDeepPurple
import com.example.alaya.ui.theme.AlayaNudeCream
import com.example.alaya.ui.theme.AlayaTan
import com.example.alaya.view.ui.theme.AlayaTheme
import com.example.alaya.viewmodel.AuthViewModel

class Registration : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                val navController = rememberNavController()
                RegistrationScreen(navController = navController)
            }
        }
    }
}

@Composable
fun RegistrationScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    // User input states part
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }

// visibility icon part
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(AlayaDeepPurple)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(size.width, size.height * 0.18f)
                cubicTo(
                    x1 = size.width * 0.7f, y1 = size.height * 0.22f,
                    x2 = size.width * 0.2f, y2 = size.height * 0.32f,
                    x3 = 0f, y3 = size.height * 0.38f
                )
                lineTo(0f, size.height)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path = path, color = AlayaNudeCream)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 35.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Begin Your", style = TextStyle(fontSize = 32.sp, fontFamily = FontFamily.Serif, color = Color.White, fontWeight = FontWeight.Light))
                Text("Journey", style = TextStyle(fontSize = 42.sp, fontFamily = FontFamily.Serif, color = Color.White, fontWeight = FontWeight.Bold))
                Text("with Alaya", style = TextStyle(fontSize = 16.sp, color = Color.White.copy(alpha = 0.7f)))
            }

            Spacer(modifier = Modifier.height(160.dp))

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 35.dp)) {
                AestheticIconInputField("User Name", fullName, { fullName = it }, Icons.Default.Person)

                Spacer(modifier = Modifier.height(25.dp))

                AestheticIconInputField("Email Address", email, { email = it }, Icons.Default.Email)

                Spacer(modifier = Modifier.height(25.dp))

                AestheticIconInputField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    showPassword = isPasswordVisible,
                    onToggleVisibility = { isPasswordVisible = !isPasswordVisible }
                )

                Spacer(modifier = Modifier.height(25.dp))

                AestheticIconInputField(
                    label = "Confirm Password",
                    value = rePassword,
                    onValueChange = { rePassword = it },
                    icon = Icons.Default.CheckCircle,
                    isPassword = true,
                    showPassword = isConfirmPasswordVisible,
                    onToggleVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible }
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                    } else if (password != rePassword) {
                        Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                    } else {
                        authViewModel.register(fullName, email, password) { success, message ->
                            if (success) {
                                Toast.makeText(context, "Registration Success!", Toast.LENGTH_SHORT).show()
                                navController.navigate("login") {
                                    popUpTo("registration") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, message ?: "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.85f).height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AlayaDeepPurple)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Join the Ritual", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Row {
                Text("Already part of Alaya? ", color = Color.Gray, fontSize = 14.sp)
                Text("Sign In", color = AlayaTan, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AestheticIconInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onToggleVisibility: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label.uppercase(), style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AlayaTan, letterSpacing = 1.sp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = AlayaDeepPurple, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))

            Box(modifier = Modifier.weight(1f)) {
                //text input logic
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 16.sp, color = AlayaDeepPurple),
                    visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text("Required", color = Color.LightGray, fontSize = 16.sp)
                        }
                        innerTextField()
                    }
                )
            }

            if (isPassword && onToggleVisibility != null) {
                IconButton(onClick = onToggleVisibility, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Visibility",
                        tint = AlayaDeepPurple.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        // divider line
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AlayaDeepPurple.copy(alpha = 0.2f)))
    }
}