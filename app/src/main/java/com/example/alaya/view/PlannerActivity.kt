package com.example.alaya.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alaya.ui.theme.AlayaTextMuted
import com.example.alaya.ui.theme.AlayaWhite
import com.example.alaya.view.ui.theme.AlayaCreamBg
import com.example.alaya.view.ui.theme.AlayaPurpleLight
import com.example.alaya.view.ui.theme.AlayaPurplePrimary
import com.example.alaya.view.ui.theme.AlayaTextDark
import com.example.alaya.view.ui.theme.AlayaTheme
import androidx.compose.material3.Icon
import androidx.navigation.NavHostController

class PlannerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                PlannerScreen(onBack = { finish() })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(navController: NavHostController? = null, onBack: () -> Unit) {
    var date by remember { mutableStateOf("22/02/2026") }
    var yogaType by remember { mutableStateOf("Vinyasa") }
    var duration by remember { mutableStateOf("30") }
    var notes by remember { mutableStateOf("") }
    val RichCream = Color(0xFFFDFBF5)

    Scaffold(
        containerColor = RichCream,
        topBar = {
            TopAppBar(
                title = {
                    Text("Yoga Planner", fontWeight = FontWeight.Bold, color = AlayaPurplePrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = AlayaPurplePrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 8.dp,
                shadowElevation = 15.dp
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(75.dp)
                ) {
                    val items = listOf(
                        Triple(Icons.Default.Home, "Home", "dashboard"),
                        Triple(Icons.Default.DateRange, "Planner", "planner"),
                        Triple(Icons.Default.Restaurant, "Meals", "meals"),
                        Triple(Icons.Default.Person, "Profile", "profile")
                    )

                    items.forEach { (icon, label, route) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(24.dp)) },
                            label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            selected = route == "planner",
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AlayaPurplePrimary,
                                selectedTextColor = AlayaPurplePrimary,
                                unselectedIconColor = AlayaTextMuted,
                                unselectedTextColor = AlayaTextMuted,
                                indicatorColor = AlayaPurpleLight.copy(alpha = 0.3f)
                            ),
                            onClick = {
                                if (route != "planner") {
                                    navController?.navigate(route)
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 22.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text("NEW PLAN", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaTextMuted)
            Text("Schedule a session", fontSize = 22.sp, fontWeight = FontWeight.Black, color = AlayaTextDark)

            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    PlannerInput(label = "DATE", value = date, onValueChange = { date = it })
                    PlannerInput(label = "YOGA TYPE", value = yogaType, onValueChange = { yogaType = it })
                    PlannerInput(label = "DURATION (MIN)", value = duration, onValueChange = { duration = it })
                    PlannerInput(
                        label = "NOTES (OPTIONAL)",
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = "e.g., Focus on hips"
                    )

                    Button(
                        onClick = {  },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AlayaPurplePrimary)
                    ) {
                        Text("ADD TO PLANNER", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            Text("UPCOMING DATES", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaTextMuted)
            Spacer(modifier = Modifier.height(12.dp))

            val dates = listOf(
                "2026-02-22", "2026-02-23",
                "2026-02-24", "2026-02-25",
                "2026-02-26", "2026-02-27"
            )

            dates.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { dateStr ->
                        DateCard(dateStr, modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun DateCard(date: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(date, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AlayaTextDark)
            Text("No sessions planned.", fontSize = 11.sp, color = AlayaTextMuted)
        }
    }
}

@Composable
fun PlannerInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = ""
) {
    Column {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaTextMuted)
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 14.sp, color = AlayaTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = AlayaPurplePrimary,
                unfocusedIndicatorColor = AlayaPurpleLight.copy(alpha = 0.5f),
                cursorColor = AlayaPurplePrimary
            ),
            singleLine = true
        )
    }
}