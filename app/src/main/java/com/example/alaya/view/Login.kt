package com.example.alaya.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.alaya.R
import com.example.alaya.ui.theme.AlayaDeepPurple
import com.example.alaya.ui.theme.AlayaDeepText
import com.example.alaya.ui.theme.AlayaNudeCream
import com.example.alaya.ui.theme.AlayaTan
import com.example.alaya.viewmodel.AuthViewModel

class Login: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            LoginUI(navController = navController)
        }

    }
}
@Composable
fun LoginUI(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AlayaDeepPurple)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .weight(0.35f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "THE PORTAL TO PEACE",
                style = TextStyle(
                    fontSize = 11.sp,
                    letterSpacing = 3.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.7f)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Welcome back",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color.White
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "to ", style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Serif, color = Color.White))
                Text(text = "Alaya.", style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Serif, color = AlayaTan))
            }
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .width(50.dp)
                    .height(2.5.dp)
                    .background(AlayaTan)
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f),
            shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp),
            color = AlayaNudeCream,
            shadowElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 35.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Access your curated flows and stillness in one breath.",
                    color = AlayaDeepText.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Email part
                AestheticIconInputField(
                    label = "Email Address",
                    value = email,
                    onValueChange = { email = it },
                    icon = Icons.Default.Email,
                    labelColor = AlayaTan
                )

                Spacer(modifier = Modifier.height(25.dp))

                // Password part
                AestheticIconInputField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    labelColor = AlayaTan
                )

                Text(
                    text = "Forgot your ritual?",
                    color = AlayaTan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .align(Alignment.End)
                        .clickable { navController.navigate("forgot") }
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Enter Button
                Button(
                    onClick = {
                        authViewModel.login(email, password,
                            onSuccess = { navController.navigate("dashboard") },
                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AlayaDeepPurple) // म्याचिङ पर्पल बटन
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Enter Portal", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("New to the ritual? ", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        text = "Join Alaya",
                        color = AlayaTan,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { navController.navigate("register") }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
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
    labelColor: Color
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            color = labelColor,
            fontSize = 10.sp,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            // Left Side Icon
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = labelColor.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Input Field
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f), // यसले बाँकी ठाउँ लिन्छ
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF2D2D2D),
                    fontWeight = FontWeight.Medium
                ),
                singleLine = true,
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None
            )

            // Visibility part
            if (isPassword) {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.size(24.dp)
                ) {
                    if (passwordVisible) {
                        Icon(
                            painter = painterResource(id = R.drawable.img_13),
                            contentDescription = "Show Password",
                            tint = AlayaTan,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = "Hide Password",
                            tint = Color.LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .height(1.2.dp)
                .background(Color.Gray.copy(alpha = 0.2f))
        )
    }
}