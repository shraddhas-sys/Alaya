package com.example.alaya.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.alaya.ui.theme.AlayaBrownBtn
import com.example.alaya.ui.theme.AlayaCream
import com.example.alaya.ui.theme.AlayaDarkText
import com.example.alaya.ui.theme.AlayaMutedText
import com.example.alaya.ui.theme.AlayaPurple
import com.example.alaya.ui.theme.AlayaPurpleLgt
import com.example.alaya.ui.theme.AlayaTextMuted
import com.example.alaya.ui.theme.RichCream
import com.example.alaya.view.ui.theme.AlayaPurpleLight
import com.example.alaya.view.ui.theme.AlayaPurplePrimary
import com.example.alaya.view.ui.theme.AlayaTextDark
import com.example.alaya.view.ui.theme.AlayaTheme



class MealPlannerScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                MealPlannerScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerScreen(navController: NavHostController) {
    var recipeInput by remember { mutableStateOf("") }
    val recipes = remember {
        mutableStateListOf(
            RecipeItem("Hamburger", "Classic Beef", "Rs. 250"),
            RecipeItem("Pepperoni Pizza", "Italian", "Rs. 350"),
            RecipeItem("Cheese Sandwich", "Quick Snack", "Rs. 200"),
            RecipeItem("Avocado Salad", "Healthy", "Rs. 300")
        )
    }

    Scaffold(
        containerColor = AlayaCream,
        topBar = {
            TopAppBar(
                title = { Text("Meal Planner", fontWeight = FontWeight.Black, color = AlayaPurple) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AlayaPurple)
                    }
                }
            )
        },
        bottomBar = {
            AlayaBottomNav(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 22.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("RECIPE BOOK", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaMutedText)
            Text("Simple ideas", fontSize = 26.sp, fontWeight = FontWeight.Black, color = AlayaDarkText)

            Spacer(modifier = Modifier.height(20.dp))

            Surface(color = Color.White, shape = RoundedCornerShape(24.dp), shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = recipeInput,
                        onValueChange = { recipeInput = it },
                        placeholder = { Text("e.g., Green Detox Bowl", fontSize = 14.sp, color = AlayaMutedText) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Button(
                        onClick = {
                            if (recipeInput.isNotBlank()) {
                                recipes.add(0, RecipeItem(recipeInput, "Custom", "Planned"))
                                recipeInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AlayaBrownBtn),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("ADD", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("POPULAR FOOD", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaMutedText)
                TextButton(onClick = { }) {
                    Text("See All", color = Color(0xFFE57373), fontWeight = FontWeight.Bold)
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                items(recipes) { recipe ->
                    PopularRecipeCard(recipe)
                }
            }
        }
    }
}

@Composable
fun PopularRecipeCard(recipe: RecipeItem) {
    Surface(color = Color.White, shape = RoundedCornerShape(24.dp), shadowElevation = 4.dp, modifier = Modifier.width(170.dp)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(18.dp)).background(AlayaPurpleLgt.copy(alpha = 0.4f))) {
                Icon(Icons.Default.Restaurant, null, modifier = Modifier.size(40.dp).align(Alignment.Center), tint = AlayaPurple)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(recipe.name, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = AlayaDarkText, maxLines = 1)
            Text(recipe.type, fontSize = 12.sp, color = AlayaMutedText)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(recipe.price, fontWeight = FontWeight.Black, color = AlayaPurple, fontSize = 14.sp)
                IconButton(onClick = { }, modifier = Modifier.size(32.dp).background(AlayaPurple, RoundedCornerShape(10.dp))) {
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

data class RecipeItem(val name: String, val type: String, val price: String)