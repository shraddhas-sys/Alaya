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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.alaya.R
import com.example.alaya.navigation.Routes
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
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFD6C1FF), Color(0xFFFFE8B3))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Alaya Logo",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Alaya",
                color = Color(0xFF5B3E96),
                fontSize = 28.sp,
                letterSpacing = 2.sp
            )
            Text(
                text = "a calm in chaos",
                color = Color(0xFF5B3E96).copy(alpha = 0.7f),
                fontSize = 16.sp
            )
        }
    }
    LaunchedEffect(Unit) {
        delay(2500)

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