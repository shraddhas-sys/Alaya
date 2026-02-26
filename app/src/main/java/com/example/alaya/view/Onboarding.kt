package com.example.alaya.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

class Onboarding : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            OnboardingUI(navController = navController)
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
        colors = listOf(Color(0xFFFFF8E1), Color(0xFFD6C1FF))
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
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = pageTitles[currentPage],
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5B3E96),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = pageSubtitles[currentPage],
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Indicates the page part
            Row(horizontalArrangement = Arrangement.Center) {
                pages.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentPage) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (index == currentPage) Color(0xFF8E44AD) else Color.Gray)
                    )
                    if (index != pages.lastIndex) Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Next and Get started buttons
            Button(
                onClick = {
                    if (currentPage < pages.lastIndex) {
                        currentPage++
                    } else {
                        sharedPreferences.edit().putBoolean("finished", true).apply()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E44AD))
            ) {
                Text(
                    text = if (currentPage < pages.lastIndex) "Next" else "Get Started",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}





