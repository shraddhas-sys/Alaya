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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.VisibilityOff
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

    // Purple Palette according to alaya
        val purplePrimary = Color(0xFF8E44AD)
        val purpleGradient = Brush.verticalGradient(
            listOf(Color(0xFF1F1C2C), Color(0xFF5D4B7A), Color(0xFF8E44AD))
        )

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            // 1. Top Header Section (40% height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .background(purpleGradient)
                    .padding(24.dp)
            ) {
                // Background circles for the "Zen" aesthetic
                Box(modifier = Modifier.size(110.dp).offset(x = (-30).dp, y = 60.dp).background(Color.White.copy(0.08f), RoundedCornerShape(60.dp)))
                Box(modifier = Modifier.size(70.dp).offset(x = 260.dp, y = 10.dp).background(Color.White.copy(0.08f), RoundedCornerShape(40.dp)))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Spacer(modifier = Modifier.height(50.dp))
                        Text("Hello", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Normal)
                        Text("Sign in!", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                    }
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(top = 55.dp).size(30.dp)
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .offset(y = (-35).dp),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                color = Color.White,
                shadowElevation = 12.dp
            ) {
                Column(modifier = Modifier.padding(horizontal = 30.dp, vertical = 35.dp)) {

                    // Email Part
                    UnderlinedInputField(
                        label = "Gmail",
                        value = email,
                        onValueChange = { email = it },
                        trailingIcon = Icons.Default.Check,
                        labelColor = purplePrimary
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    // Password Part
                    UnderlinedInputField(
                        label = "Password",
                        value = password,
                        onValueChange = { password = it },
                        trailingIcon = Icons.Default.VisibilityOff,
                        isPassword = true,
                        labelColor = purplePrimary
                    )
                    Text(
                        text = "Forgot Password?",
                        color = purplePrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .align(Alignment.End)
                            .clickable { navController.navigate("forgot") }
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = {
                            authViewModel.login(email, password,
                                onSuccess = { navController.navigate("dashboard") },
                                onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(listOf(Color(0xFF9C4DB9), Color(0xFF6C3483))),
                                    RoundedCornerShape(28.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("SIGN IN", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Divider(modifier = Modifier.weight(1f), color = Color.Gray.copy(0.2f))
                        Text("  OR  ", color = Color.Gray, fontSize = 12.sp)
                        Divider(modifier = Modifier.weight(1f), color = Color.Gray.copy(0.2f))
                    }

                    Spacer(modifier = Modifier.height(25.dp))
                    OutlinedButton(
                        onClick = { /* Google logic */ },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(1.dp, Color.LightGray.copy(0.5f))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.img_8),
                                contentDescription = "Google",
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Sign up with Google", color = Color.Black.copy(0.7f), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("Don't have account? ", color = Color.Gray, fontSize = 14.sp)
                        Text(
                            text = "Sign up",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { navController.navigate("register") }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun UnderlinedInputField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        trailingIcon: ImageVector,
        isPassword: Boolean = false,
        labelColor: Color
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = label, color = labelColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    singleLine = true,
                    visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
                )
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = Color.Gray.copy(0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
            Divider(color = Color.Gray.copy(0.2f), thickness = 1.dp, modifier = Modifier.padding(top = 4.dp))
        }
    }