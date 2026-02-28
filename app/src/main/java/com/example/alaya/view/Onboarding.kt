package com.example.alaya.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alaya.R
import com.example.alaya.navigation.Routes
import androidx.core.content.edit
import com.example.alaya.ui.theme.AlayaDeepPurple
import com.example.alaya.ui.theme.primaryColor
import com.example.alaya.view.ui.theme.AlayaTheme

class Onboarding : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                val navController = rememberNavController()
                OnboardingUI(navController = navController)
            }
        }
    }
}

@Composable
fun OnboardingUI(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("alaya_prefs", Context.MODE_PRIVATE)

    val pages = listOf(R.drawable.img_1, R.drawable.img_3, R.drawable.img_2)
    val pageTitles = listOf(
        "Help you to make schedule for yoga.",
        "Track your progress daily.",
        "Discover new yoga postures."
    )
    val pageSubtitles = listOf(
        "Sukhasana - Easy Pose",
        "Stay consistent with your routine",
        "Learn postures like Warrior !!"
    )

    var currentPage by remember { mutableIntStateOf(0) }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF8E1),
            Color(0xFFD6C1FF)
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = pages[currentPage]),
                contentDescription = "Yoga Illustration",
                modifier = Modifier.size(280.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = pageTitles[currentPage],
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = primaryColor,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = pageSubtitles[currentPage],
                fontSize = 15.sp,
                color = Color.DarkGray.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Page Indicators
            Row(horizontalArrangement = Arrangement.Center) {
                pages.forEachIndexed { index, _ ->
                    val isSelected = index == currentPage
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (isSelected) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) primaryColor else Color.Gray.copy(alpha = 0.5f))
                    )
                    if (index != pages.lastIndex) Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    if (currentPage < pages.lastIndex) {
                        currentPage++
                    } else {
                        sharedPreferences.edit { putBoolean("finished", true) }
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text(
                    text = if (currentPage < pages.lastIndex) "Next" else "Get Started",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}