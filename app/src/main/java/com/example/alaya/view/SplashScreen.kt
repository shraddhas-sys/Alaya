package com.example.alaya.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.alaya.R
import com.example.alaya.navigation.Routes
import com.example.alaya.ui.theme.alayaLightPurplePrimary
import com.example.alaya.ui.theme.alayaLightPurpleSecondary
import com.example.alaya.ui.theme.alayaSoftTextPurple
import com.example.alaya.ui.theme.alayaUltraLightCream
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashScreen()
        }
    }
}
@Composable
fun SplashUI(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(alayaLightPurplePrimary, alayaLightPurpleSecondary)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Alaya Logo",
                modifier = Modifier.size(200.dp)
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(
                    color = alayaUltraLightCream,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 35.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(45.dp)
                        .height(5.dp)
                        .background(alayaLightPurplePrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.height(35.dp))

                Text(
                    text = "Discover Alaya",
                    color = alayaSoftTextPurple,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Your personal culinary companion. Experience the art of mindful eating with the freshest ingredients delivered to your door.",
                    color = alayaSoftTextPurple.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(30.dp))

                // सानो ट्यागलाइन
                Text(
                    text = "Fresh • Simple • Organic",
                    color = alayaLightPurplePrimary.copy(alpha = 1f),
                    style = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = Color(0xFF7E57C2) // अलि गाढा पर्पल हाइलाइटको लागि
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(3000)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val sharedPreferences = context.getSharedPreferences("alaya_prefs", Context.MODE_PRIVATE)
        val onboardingFinished = sharedPreferences.getBoolean("finished", false)

        when {
            currentUser != null -> {
                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
            onboardingFinished -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
            else -> {
                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }
    }
}