package com.example.alaya.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.alaya.R
import com.example.alaya.viewmodel.DashboardViewModel
import com.example.alaya.ui.theme.*

class DashboardUI : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                val navController = rememberNavController()
                val dashboardViewModel: DashboardViewModel = viewModel()
                DashboardUI(navController = navController, dashboardViewModel = dashboardViewModel)
            }
        }
    }
}

@Composable
fun DashboardUI(navController: NavHostController, dashboardViewModel: DashboardViewModel) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Fetch user data
    LaunchedEffect(Unit) {
        dashboardViewModel.fetchUserData()
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                dashboardViewModel.logout {
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            }
        )
    }

    Scaffold(
        containerColor = AlayaNudeCream,
        bottomBar = { AlayaBottomNav(navController, onLogoutClick = { showLogoutDialog = true }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 22.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header Section
            HeaderSection(
                name = dashboardViewModel.userName.value,
                unreadCount = dashboardViewModel.unreadCount,
                onNotificationClick = { navController.navigate("notifications") },
                onUndoClick = { dashboardViewModel.deleteLastPlan() }
            )

            SectionHeader("Popular Workout")
            val workouts = listOf(
                "Adho Mukha Svanasana" to R.drawable.img_9,
                "Sleep Yoga" to R.drawable.img_14,
                "Binaysan" to R.drawable.img_12
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(workouts) { (title, imgRes) -> WorkoutCard(title, imgRes) }
            }

            SectionHeader("Start Planning")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(listOf("Hatha", "Vinyasa", "Yin", "Ashtanga")) { style ->
                    PlanningChip(style) {
                        navController.navigate("planner")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

           // Ritual Progress Card
            RitualProgressCard(
                planned = dashboardViewModel.planned.value,
                streak = dashboardViewModel.streak.value,
                goal = dashboardViewModel.totalGoal.value
            )

            SectionHeader("Healthy Meal Ideas")
            val meals = listOf(
                Triple("Yogurt Bowl", "15 min", R.drawable.img_10),
                Triple("Avocado and Egg Breakfast Plate", "10 min", R.drawable.img_11)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(meals) { (name, time, imgRes) ->
                    MealCard(name, time, imgRes) { navController.navigate("meals") }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

        // Action Button
            Button(
                onClick = {
                    navController.navigate("planner")
                },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AlayaDeepPurple)
            ) {
                Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(24.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text("GO TO PLANNER", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
// ritual progress part
@Composable
fun RitualProgressCard(planned: Int, streak: Int, goal: Int) {
    val progressFactor = if (goal > 0) planned.toFloat() / goal.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progressFactor.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000),
        label = "progressAnimation"
    )
    val percentage = (progressFactor * 100).toInt().coerceIn(0, 100)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 0.5.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("Ritual Progress", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = AlayaDeepPurple)
            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("COMPLETED", planned.toString(), Modifier.weight(1f))
                StatCard("STREAK", streak.toString(), Modifier.weight(1f), isHighlight = true)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Current progress", fontSize = 11.sp, color = Color.Gray)
                    Text("$percentage%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AlayaDeepPurple)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = AlayaDeepPurple,
                    trackColor = AlayaDeepPurple.copy(alpha = 0.1f),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
fun HeaderSection(name: String, unreadCount: Int, onNotificationClick: () -> Unit, onUndoClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 0.5.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(AlayaDeepPurple.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(id = R.drawable.img), contentDescription = "Logo", tint = AlayaDeepPurple, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(if (name.isNotEmpty()) "Namaste, $name" else "Namaste!", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Text("Alaya Yoga", fontSize = 18.sp, fontWeight = FontWeight.Black, color = AlayaDeepPurple)
            }

            IconButton(onClick = onUndoClick) {
                Icon(Icons.Outlined.Undo, contentDescription = "Undo", tint = Color.Red.copy(alpha = 0.7f))
            }

            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(40.dp).background(AlayaNudeCream, CircleShape)
            ) {
                BadgedBox(badge = { if (unreadCount > 0) Badge(containerColor = AlayaDeepPurple, contentColor = Color.White) { Text(unreadCount.toString()) } }) {
                    Icon(Icons.Default.Notifications, null, tint = AlayaDeepPurple, modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}
// Stat Card
@Composable
fun StatCard(title: String, value: String, modifier: Modifier, isHighlight: Boolean = false) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(if (isHighlight) AlayaDeepPurple else AlayaDeepPurple.copy(alpha = 0.08f)).padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isHighlight) Color.White.copy(alpha = 0.8f) else AlayaDeepPurple.copy(alpha = 0.6f))
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = if (isHighlight) Color.White else AlayaDeepPurple)
    }
}

@Composable
fun AlayaBottomNav(navController: NavHostController, onLogoutClick: () -> Unit) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(color = Color.White, shadowElevation = 15.dp) {
        NavigationBar(containerColor = Color.White, modifier = Modifier.navigationBarsPadding().height(75.dp)) {
            val navItems = listOf(
                Triple(Icons.Default.Home, "Home", "dashboard"),
                Triple(Icons.Default.DateRange, "Planner", "planner"),
                Triple(Icons.Default.Restaurant, "Meals", "meals"),
                Triple(Icons.Default.Person, "Profile", "profile"),
                Triple(Icons.Default.ExitToApp, "Logout", "logout")
            )
            navItems.forEach { (icon, label, route) ->
                val isSelected = currentRoute == route
                NavigationBarItem(
                    icon = { Icon(icon, null, modifier = Modifier.size(if(isSelected) 28.dp else 24.dp)) },
                    label = { Text(label, fontSize = 10.sp, fontWeight = if(isSelected) FontWeight.ExtraBold else FontWeight.Bold) },
                    selected = isSelected,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AlayaDeepPurple, selectedTextColor = AlayaDeepPurple, indicatorColor = AlayaDeepPurple.copy(alpha = 0.15f), unselectedIconColor = Color.Gray.copy(alpha = 0.5f)),
                    onClick = {
                        if (route == "logout") onLogoutClick()
                        else if (currentRoute != route) {
                            navController.navigate(route) { popUpTo("dashboard") { saveState = true }; launchSingleTop = true; restoreState = true }
                        }
                    }
                )
            }
        }
    }
}
// Workout Card
@Composable
fun WorkoutCard(title: String, imageResId: Int) {
    Box(modifier = Modifier.width(200.dp).height(115.dp).clip(RoundedCornerShape(22.dp))) {
        Image(painter = painterResource(id = imageResId), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)))))
        Text(title, modifier = Modifier.align(Alignment.BottomStart).padding(14.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
// Meal Card
@Composable
fun MealCard(name: String, time: String, imageResId: Int, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.White, shape = RoundedCornerShape(20.dp), shadowElevation = 0.5.dp, modifier = Modifier.width(160.dp)) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))) {
                Image(painter = painterResource(id = imageResId), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(name, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = AlayaDeepPurple, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, null, modifier = Modifier.size(12.dp), tint = Color.Gray); Spacer(modifier = Modifier.width(4.dp))
                    Text(time, fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(modifier = Modifier.padding(top = 22.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(4.dp, 18.dp).clip(CircleShape).background(AlayaDeepPurple)); Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Black, color = AlayaDeepPurple)
    }
}
// Planning Chip
@Composable
fun PlanningChip(name: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = AlayaDeepPurple.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp), modifier = Modifier.width(100.dp).height(80.dp)) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.PlayArrow, null, tint = AlayaDeepPurple, modifier = Modifier.size(20.dp)); Spacer(modifier = Modifier.height(6.dp))
            Text(name, fontWeight = FontWeight.Bold, color = AlayaDeepPurple, fontSize = 13.sp)
        }
    }
}
// Logout Confirmation
@Composable
fun LogoutConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(28.dp), color = Color.White, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(64.dp).background(AlayaDeepPurple.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.ExitToApp, null, tint = AlayaDeepPurple, modifier = Modifier.size(30.dp)) }
                Spacer(modifier = Modifier.height(20.dp))
                Text("Come back soon!", fontSize = 20.sp, fontWeight = FontWeight.Black, color = AlayaDeepPurple)
                Spacer(modifier = Modifier.height(10.dp)); Text("Are you sure you want to log out?", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(30.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, AlayaDeepPurple.copy(alpha = 0.3f))) { Text("Stay", color = AlayaDeepPurple, fontWeight = FontWeight.Bold) }
                    Button(onClick = onConfirm, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = AlayaDeepPurple)) { Text("Logout", color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}