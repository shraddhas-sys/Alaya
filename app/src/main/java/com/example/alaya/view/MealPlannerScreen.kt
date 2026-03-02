package com.example.alaya.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alaya.R
import com.example.alaya.model.LocalMealItem
import com.example.alaya.repository.MealRepository
import com.example.alaya.repository.MealRepositoryImpl
import com.example.alaya.ui.theme.*
import com.example.alaya.view.ui.theme.AlayaTheme
import com.example.alaya.viewmodel.MealViewModel

class MealPlannerScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                MealPlannerContent(onBack = { finish() })
            }
        }
    }
}

//Factory class
class MealViewModelFactory(private val repo: MealRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerContent(
    onBack: () -> Unit,
    vm: MealViewModel = viewModel(
        factory = MealViewModelFactory(MealRepositoryImpl())
    )
) {
    // States for form inputs
    var mealName by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("Lunch") }
    var proteinValue by remember { mutableFloatStateOf(75f) }

    val addedMeals by vm.meals.collectAsState(initial = emptyList())
    val mealTypes = listOf("Breakfast", "Dinner", "Lunch", "Snack", "Brunch", "Dessert")


    val primaryColor = AlayaDeepPurple
    val backgroundColor = AlayaNudeCream

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MEAL PLANNER",
                        fontWeight = FontWeight.Black,
                        color = primaryColor,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = primaryColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text("POPULAR CHOICES", fontSize = 11.sp, fontWeight = FontWeight.Black, color = primaryColor.copy(0.6f))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                val popular = listOf(
                    Pair("Yogurt Bowl", R.drawable.img_10),
                    Pair("Avocado and Egg", R.drawable.img_11)
                )
                items(popular) { (name, resId) ->
                    PopularMealCard(name, resId, primaryColor)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main card for adding a new meal

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(28.dp),
                shadowElevation = 0.5.dp
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    // Meal name input
                    Column {
                        Text("MEAL NAME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = mealName,
                            onValueChange = { mealName = it },
                            placeholder = { Text("What are you eating?", fontSize = 14.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = primaryColor.copy(0.1f)
                            ),
                            singleLine = true
                        )
                    }

                    // grid to pick meal category
                    SelectionGrid(mealTypes, listOf(selectedMealType), { selectedMealType = it }, primaryColor)

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("PROTEIN TARGET", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                            Spacer(modifier = Modifier.weight(1f))
                            Text("${proteinValue.toInt()}g", fontWeight = FontWeight.Black, color = primaryColor)
                        }
                        Slider(
                            value = proteinValue,
                            onValueChange = { proteinValue = it },
                            valueRange = 0f..150f,
                            colors = SliderDefaults.colors(
                                thumbColor = primaryColor,
                                activeTrackColor = primaryColor,
                                inactiveTrackColor = primaryColor.copy(0.1f)
                            )
                        )
                    }

                    Button(
                        onClick = {
                            if (mealName.isNotBlank()) {
                                vm.addMeal(mealName, selectedMealType, "${proteinValue.toInt()}g Protein")
                                mealName = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(58.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(18.dp)
                    ) { Text("ADD TO PLAN", fontWeight = FontWeight.ExtraBold, color = Color.White) }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(4.dp, 16.dp).clip(CircleShape).background(primaryColor))
                Spacer(modifier = Modifier.width(8.dp))
                Text("TODAY'S SCHEDULE", fontSize = 14.sp, fontWeight = FontWeight.Black, color = primaryColor)
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (addedMeals.isEmpty()) {
                Text("No meals added yet.", modifier = Modifier.padding(vertical = 20.dp), color = Color.Gray, fontSize = 14.sp)
            } else {
                addedMeals.forEach { meal ->
                    AddedMealCard(
                        item = meal,
                        themeColor = primaryColor,
                        onDelete = { vm.deleteMeal(meal.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
// Single meal item row layout
@Composable
fun AddedMealCard(item: LocalMealItem, themeColor: Color, onDelete: () -> Unit) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 0.5.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(themeColor.copy(0.08f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Restaurant, null, tint = themeColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AlayaTextDark)
                Text(item.type, fontSize = 12.sp, color = Color.Gray)
            }
            Text(item.protein, fontWeight = FontWeight.Black, fontSize = 13.sp, color = themeColor)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color.Red.copy(0.4f), modifier = Modifier.size(20.dp))
            }
        }
    }
}
// PopularMealCard
@Composable
fun PopularMealCard(name: String, imageRes: Int, themeColor: Color) {
    Surface(
        modifier = Modifier.size(145.dp, 185.dp),
        shape = RoundedCornerShape(26.dp),
        color = Color.White,
        shadowElevation = 0.5.dp
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier.fillMaxWidth().height(125.dp).clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)),
                contentScale = ContentScale.Crop
            )
            Text(name, modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Black, fontSize = 14.sp, color = themeColor)
        }
    }
}
// Selection grid
@Composable
fun SelectionGrid(items: List<String>, selected: List<String>, onSelect: (String) -> Unit, color: Color) {
    Column {
        items.chunked(3).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { item ->
                    Row(
                        modifier = Modifier.weight(1f).clickable { onSelect(item) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selected.contains(item),
                            onCheckedChange = { onSelect(item) },
                            colors = CheckboxDefaults.colors(checkedColor = color),
                            modifier = Modifier.scale(0.85f)
                        )
                        Text(item, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = if(selected.contains(item)) color else Color.Gray)
                    }
                }
            }
        }
    }
}