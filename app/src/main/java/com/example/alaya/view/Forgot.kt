package com.example.alaya.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alaya.ui.theme.backgroundColor
import com.example.alaya.ui.theme.primaryColor
import com.example.alaya.viewmodel.AuthViewModel


class Forgot : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Forgot()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Forgot(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    // Track user email for reset
    var email by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFF3E5AB), Color(0xFFB39DDB))
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFB39DDB), Color(0xFFF3E5AB))
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Recovery Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.size(30.dp).padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Forgot Password",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = primaryColor
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Enter the email address associated with your account",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                        // Text Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Email Address", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = primaryColor.copy(alpha = 0.6f)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                   // Button part
                    Button(
                        onClick = {
                            if (email.isNotBlank()) {
                                authViewModel.forgotPassword(email) { success, error ->
                                    if (success) {
                                        Toast.makeText(context, "Reset link sent!", Toast.LENGTH_LONG).show()
                                        navController.navigate("login") {
                                            popUpTo("forgot") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(context, error ?: "Failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text(
                            "Recover Password",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Back to Login",
                        color = primaryColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { navController.navigate("login") }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}