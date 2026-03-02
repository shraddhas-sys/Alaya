package com.example.alaya.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alaya.model.AlayaNotification
import com.example.alaya.ui.theme.*
import com.example.alaya.viewmodel.DashboardViewModel

class AlayaNotificationSheet : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                val navController = rememberNavController()
                AlayaNotificationScreen(
                    navController = navController,
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlayaNotificationScreen(
    navController: NavHostController,
    onBack: (() -> Unit)? = null,
    viewModel: DashboardViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.fetchUserData()
    }

    val notificationsList = viewModel.notifications
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Sessions", "Meals")
    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AlayaNudeCream,
        topBar = {
            TopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.Black, color = AlayaDeepPurple) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (onBack != null) onBack() else navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = AlayaDeepPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // Filter Tabs
            LazyRow(
                modifier = Modifier.padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        onClick = {
                            selectedFilter = filter
                            isExpanded = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) AlayaDeepPurple.copy(alpha = 0.12f) else Color.White,
                        border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray.copy(0.3f))
                    ) {
                        Text(
                            text = filter,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) AlayaDeepPurple else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Notification Card Container
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                shadowElevation = 0.5.dp
            ) {
                val filteredList = when (selectedFilter) {
                    "Sessions" -> notificationsList.filter {
                        it.title.contains("Yoga", true) ||
                                it.title.contains("Session", true) ||
                                it.title.contains("Plan", true)
                    }
                    "Meals" -> notificationsList.filter {
                        it.title.contains("Meal", true) ||
                                it.title.contains("Diet", true) ||
                                it.message.contains("Eating", true)
                    }
                    else -> notificationsList
                }

                if (filteredList.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No activity for $selectedFilter", color = Color.Gray, fontSize = 14.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.animateContentSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        val displayCount = if (isExpanded) filteredList.size else minOf(filteredList.size, 5)
                        val displayList = filteredList.take(displayCount)

                        itemsIndexed(
                            items = displayList,
                            key = { _, item -> item.id }
                        ) { index, notification ->
                            NotificationItemRow(
                                notification = notification,
                                themeColor = AlayaDeepPurple,
                                onReadClick = {
                                    if (notification.id.isNotEmpty()) {
                                        viewModel.markAsRead(notification.id)
                                    }
                                }
                            )
                            if (index < displayList.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = Color.LightGray.copy(0.2f)
                                )
                            }
                        }

                        if (filteredList.size > 5) {
                            item {
                                TextButton(
                                    onClick = { isExpanded = !isExpanded },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (isExpanded) "Show Less" else "Show More (${filteredList.size - 5}+)",
                                        color = AlayaDeepPurple,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun NotificationItemRow(
    notification: AlayaNotification,
    themeColor: Color,
    onReadClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onReadClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    if (notification.isRead) Color.Gray.copy(alpha = 0.05f)
                    else themeColor.copy(alpha = 0.1f),
                    CircleShape
                )
                .border(1.dp, themeColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Notifications,
                null,
                tint = if (notification.isRead) Color.LightGray else themeColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = notification.title,
                    fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = if (notification.isRead) Color.Gray else Color(0xFF1A1A1A)
                )
                if (!notification.isRead) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(6.dp).background(themeColor, CircleShape))
                }
            }
            Text(
                text = notification.message,
                color = if (notification.isRead) Color.LightGray else Color.DarkGray,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Text(
            text = notification.timeAgo,
            color = Color.LightGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}