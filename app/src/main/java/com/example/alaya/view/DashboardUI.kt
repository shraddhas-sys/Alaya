package com.example.alaya.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alaya.viewmodel.DashboardViewModel
import com.example.alaya.R
import com.example.alaya.ui.theme.AlayaBackground
import com.example.alaya.ui.theme.AlayaCreamBg
import com.example.alaya.ui.theme.AlayaPrimary
import com.example.alaya.ui.theme.AlayaPurpleLight
import com.example.alaya.ui.theme.AlayaPurplePrimary
import com.example.alaya.ui.theme.AlayaSurface
import com.example.alaya.ui.theme.AlayaTextDark
import com.example.alaya.ui.theme.AlayaTextMuted
import com.example.alaya.ui.theme.AlayaTextSecondary
import com.example.alaya.ui.theme.AlayaWhite
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun DashboardUI(navController: NavHostController) {
    val viewModel: DashboardViewModel = viewModel()
    val hasActivePlan = true

    Scaffold(
        containerColor = AlayaCreamBg,
        bottomBar = { AlayaBottomNav(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 22.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderSection(viewModel.userName.value)

            SectionHeader("Popular Workout")
            val workouts = listOf(
                "Zen Challenge" to R.drawable.img_9,
                "Sleep Yoga" to R.drawable.img_1
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(workouts) { (title, imgRes) ->
                    WorkoutCard(title, imgRes)
                }
            }

            SectionHeader("Start Planning")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(listOf("Hatha", "Vinyasa", "Yin", "Ashtanga")) { style ->
                    PlanningChip(style) { navController.navigate("planner") }
                }
            }

            if (hasActivePlan) {
                Spacer(modifier = Modifier.height(20.dp))
                RitualProgressCard()
            }

            SectionHeader("Healthy Meal Ideas")
            val meals = listOf(
                Triple("Green Detox Bowl", "15 min", R.drawable.img_10),
                Triple("Oatmeal Bowl", "10 min", R.drawable.img_11)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(meals) { (name, time, imgRes) ->
                    MealCard(name, time, imgRes) {
                        navController.navigate("meals")
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { navController.navigate("planner") },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AlayaPurplePrimary)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("START NEW PLAN", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@Composable
fun MealCard(name: String, time: String, imageResId: Int, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
        modifier = Modifier.width(160.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(name, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = AlayaTextDark, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, null, modifier = Modifier.size(12.dp), tint = AlayaTextMuted)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(time, fontSize = 11.sp, color = AlayaTextMuted)
                }
            }
        }
    }
}

@Composable
fun AlayaBottomNav(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(color = Color.White, tonalElevation = 8.dp, shadowElevation = 15.dp) {
        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier.navigationBarsPadding().height(75.dp)
        ) {
            val navItems = listOf(
                Triple(Icons.Default.Home, "Home", "dashboard"),
                Triple(Icons.Default.DateRange, "Planner", "planner"),
                Triple(Icons.Default.Restaurant, "Meals", "meals"),
                Triple(Icons.Default.Person, "Profile", "profile")
            )
            navItems.forEach { (icon, label, route) ->
                NavigationBarItem(
                    icon = { Icon(icon, null, modifier = Modifier.size(24.dp)) },
                    label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    selected = currentRoute == route,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AlayaPurplePrimary,
                        selectedTextColor = AlayaPurplePrimary,
                        indicatorColor = AlayaPurpleLight.copy(alpha = 0.3f),
                        unselectedIconColor = AlayaTextMuted
                    ),
                    onClick = {
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo("dashboard") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun WorkoutCard(title: String, imageResId: Int) {
    Box(modifier = Modifier.width(200.dp).height(115.dp).clip(RoundedCornerShape(22.dp))) {
        Image(painter = painterResource(id = imageResId), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)))))
        Text(title, modifier = Modifier.align(Alignment.BottomStart).padding(14.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun HeaderSection(name: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("Namaste, $name", fontSize = 14.sp, color = AlayaTextMuted)
            Text("Alaya Yoga", fontSize = 26.sp, fontWeight = FontWeight.Black, color = AlayaPurplePrimary)
        }
        IconButton(onClick = {}, modifier = Modifier.size(46.dp).background(Color.White, CircleShape)) {
            Icon(Icons.Default.Notifications, null, tint = AlayaPurplePrimary)
        }
    }
}

@Composable
fun RitualProgressCard() {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shape = RoundedCornerShape(24.dp), shadowElevation = 2.dp) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("Ritual Progress", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = AlayaTextDark)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("COMPLETED", "12", Modifier.weight(1f))
                StatCard("STREAK", "5", Modifier.weight(1f), isHighlight = true)
            }
            Spacer(modifier = Modifier.height(14.dp))
            WeeklyConsistencySection()
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier, isHighlight: Boolean = false) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(if (isHighlight) AlayaPurplePrimary else AlayaPurpleLight.copy(alpha = 0.3f)).padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isHighlight) Color.White.copy(alpha = 0.8f) else AlayaPurplePrimary)
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Black, color = if (isHighlight) Color.White else AlayaTextDark)
    }
}

@Composable
fun WeeklyConsistencySection() {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Weekly progress", fontSize = 11.sp, color = AlayaTextMuted)
            Text("25%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AlayaPurplePrimary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(progress = { 0.25f }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape), color = AlayaPurplePrimary, trackColor = AlayaPurpleLight.copy(alpha = 0.3f))
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(text = title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = AlayaTextDark, modifier = Modifier.padding(top = 15.dp, bottom = 10.dp))
}

@Composable
fun PlanningChip(name: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = AlayaPurpleLight.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp), modifier = Modifier.width(100.dp).height(80.dp)) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.PlayArrow, null, tint = AlayaPurplePrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(name, fontWeight = FontWeight.Bold, color = AlayaPurplePrimary, fontSize = 13.sp)
        }
    }
}