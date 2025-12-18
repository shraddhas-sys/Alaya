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
import com.example.alaya.ui.theme.AlayaPrimary
import com.example.alaya.ui.theme.AlayaSurface
import com.example.alaya.ui.theme.AlayaTextDark
import com.example.alaya.ui.theme.AlayaTextSecondary


@Composable
fun DashboardUI(navController: NavHostController) {
        val viewModel: DashboardViewModel = viewModel()
        val scrollState = rememberScrollState()

        Scaffold(
            containerColor = AlayaBackground,
            bottomBar = { AlayaBottomNav() }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "WELCOME BACK,",
                            fontSize = 11.sp,
                            letterSpacing = 1.sp,
                            color = AlayaTextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "ALAYA",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Light,
                            color = AlayaTextDark
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(AlayaSurface, CircleShape)
                            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Search your practice...", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AlayaPrimary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = AlayaSurface,
                        focusedContainerColor = AlayaSurface,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = AlayaPrimary.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val tags = listOf("All", "Yoga", "Meditation", "Breathing", "Stretching")
                    items(tags) { tag ->
                        val isSelected = viewModel.selectedCategory.value == tag
                        Surface(
                            onClick = { viewModel.onCategorySelected(tag) },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) AlayaPrimary else Color.Transparent,
                            border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray),
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                color = if (isSelected) Color.White else AlayaTextDark,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Featured Practices",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = AlayaTextDark
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(3) {
                        YogaCard(
                            title = "Inner Balance",
                            imageResId = R.drawable.img_5
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = "Beginners Path", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Text(text = "See all", fontSize = 12.sp, color = AlayaPrimary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))

                LargeYogaItem(
                    title = "Morning Vinyasa Flow",
                    subtitle = "15 mins • Level 1",
                    imageResId = R.drawable.img_6
                )
                LargeYogaItem(
                    title = "Deep Tissue Release",
                    subtitle = "20 mins • Level 1",
                    imageResId = R.drawable.img_5
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    @Composable
    fun YogaCard(title: String, imageResId: Int) {
        Card(
            modifier = Modifier.width(220.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AlayaSurface)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.2f), CircleShape)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Rejuvenate your soul", fontSize = 12.sp, color = AlayaTextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, modifier = Modifier.size(14.dp), tint = AlayaPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("4.9 Rating", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    @Composable
    fun LargeYogaItem(title: String, subtitle: String, imageResId: Int) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            color = AlayaSurface,
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Text(text = subtitle, fontSize = 12.sp, color = AlayaTextSecondary)
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.background(AlayaBackground, CircleShape).size(32.dp)
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = AlayaPrimary)
                }
            }
        }
    }

    @Composable
    fun AlayaBottomNav() {
        NavigationBar(
            containerColor = AlayaSurface,
            tonalElevation = 8.dp
        ) {
            val items = listOf(
                Icons.Default.Home to "Home",
                Icons.Default.PlayArrow to "Practice",
                Icons.Default.FavoriteBorder to "Saved",
                Icons.Default.Person to "Profile"
            )
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.first, contentDescription = item.second) },
                    selected = index == 0,
                    onClick = {},
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        indicatorColor = AlayaPrimary,
                        unselectedIconColor = AlayaTextSecondary
                    )
                )
            }
        }
    }