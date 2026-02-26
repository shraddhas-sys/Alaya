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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.alaya.R
import com.example.alaya.navigation.Routes
import com.example.alaya.ui.theme.AlayaCream
import com.example.alaya.ui.theme.AlayaDarkPurple
import com.example.alaya.ui.theme.AlayaInactiveDot
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AlayaCream)
    ) {
        //Header or Top section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f),
            contentAlignment = Alignment.Center
        ) {
            // Purple Background Shape
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-40).dp)
                    .clip(RoundedCornerShape(bottomStart = 250.dp, bottomEnd = 250.dp))
                    .background(AlayaDarkPurple)
            )

           // Logo part
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Alaya Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp),
                contentScale = ContentScale.Fit
            )
        }

        //Welcome Text
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 240.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Alaya",
                color = Color(0xFF2D2D2D),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your journey to organic wellness\nbegins with a single step.",
                color = Color.Gray,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 50.dp)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AlayaDarkPurple))
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AlayaInactiveDot))
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AlayaInactiveDot))
        }
    }

    LaunchedEffect(Unit) {
        delay(3500)
        navController.navigate(Routes.ONBOARDING) {
            popUpTo(Routes.SPLASH) { inclusive = true }
        }
    }
}