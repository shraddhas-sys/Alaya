package com.example.alaya.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.font.FontStyle
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.compose.rememberNavController
import com.example.alaya.ui.theme.AlayaDeepPurple
import com.example.alaya.ui.theme.AlayaNudeCream
import com.example.alaya.ui.theme.AlayaTan
import com.example.alaya.view.ui.theme.AlayaTheme

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

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 35.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Begin Your",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color.White,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Journey",
                    style = TextStyle(
                        fontSize = 42.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                )
                Text(
                    text = "with Alaya",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 1.sp
                    )
                )
            }

           // input Boxes
            Spacer(modifier = Modifier.height(180.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
            ) {
                AestheticIconInputField(
                    label = "User Name",
                    value = fullName,
                    onValueChange = { fullName = it },
                    icon = Icons.Default.Person,
                    labelColor = AlayaTan
                )

                Spacer(modifier = Modifier.height(25.dp))

                AestheticIconInputField(
                    label = "Email Address",
                    value = email,
                    onValueChange = { email = it },
                    icon = Icons.Default.Email,
                    labelColor = AlayaTan
                )

                Spacer(modifier = Modifier.height(25.dp))

                AestheticIconInputField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    labelColor = AlayaTan
                )

                Spacer(modifier = Modifier.height(25.dp))

                AestheticIconInputField(
                    label = "Confirm Password",
                    value = rePassword,
                    onValueChange = { rePassword = it },
                    icon = Icons.Default.CheckCircle,
                    isPassword = true,
                    labelColor = AlayaTan
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

           // Button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(58.dp),
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

           // this is bottom link
            Row {
                Text("Already part of Alaya? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Sign In",
                    color = AlayaTan,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}