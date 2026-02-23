package com.example.alaya.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.alaya.model.LocalMealItem
import com.example.alaya.ui.theme.AlayaLavender
import com.example.alaya.ui.theme.LavenderLight
import com.example.alaya.ui.theme.SoftCream
import com.example.alaya.view.ui.theme.AlayaTheme
import com.example.alaya.viewmodel.MealViewModel
import com.example.alaya.R


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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerContent(
    onBack: () -> Unit,
    vm: MealViewModel = viewModel()
) {
    var mealName by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("Lunch") }
    var proteinValue by remember { mutableFloatStateOf(75f) }
    val addedMeals by vm.meals.collectAsState(initial = emptyList())
    val mealTypes = listOf("Breakfast", "Dinner", "Lunch", "Snack", "Brunch", "Dessert")

    Scaffold(
        containerColor = SoftCream,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, AlayaLavender.copy(0.2f))
                        ) {
                            Text(
                                text = "EAT",
                                color = AlayaLavender,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PLANNER",
                            fontWeight = FontWeight.ExtraBold,
                            color = AlayaLavender,
                            fontSize = 18.sp,
                            letterSpacing = 1.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = AlayaLavender)
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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text("POPULAR CHOICES", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AlayaLavender.copy(0.7f))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                val popular = listOf(
                    Pair("Oatmeal", R.drawable.img_10),
                    Pair("Salad", R.drawable.img_11)
                )
                items(popular) { (name, resId) ->
                    PopularMealCard(name, resId)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(color = Color.White, shape = RoundedCornerShape(28.dp), border = BorderStroke(1.dp, LavenderLight)) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Column {
                        Text("MEAL NAME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AlayaLavender)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = mealName,
                            onValueChange = { mealName = it },
                            placeholder = { Text("What are you eating?", fontSize = 14.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AlayaLavender,
                                unfocusedBorderColor = LavenderLight,
                                focusedContainerColor = SoftCream.copy(0.3f),
                                unfocusedContainerColor = SoftCream.copy(0.3f)
                            ),
                            singleLine = true
                        )
                    }

                    SelectionGrid(mealTypes, listOf(selectedMealType), { selectedMealType = it }, AlayaLavender)

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("PROTEIN TARGET", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AlayaLavender)
                            Spacer(modifier = Modifier.weight(1f))
                            Surface(color = SoftCream, shape = CircleShape) {
                                Text("${proteinValue.toInt()}g", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontWeight = FontWeight.Bold, color = AlayaLavender, fontSize = 12.sp)
                            }
                        }
                        Slider(
                            value = proteinValue,
                            onValueChange = { proteinValue = it },
                            valueRange = 0f..150f,
                            colors = SliderDefaults.colors(thumbColor = AlayaLavender, activeTrackColor = AlayaLavender, inactiveTrackColor = SoftCream)
                        )
                    }

                    Button(
                        onClick = {
                            if (mealName.isNotBlank()) {
                                vm.addMeal(mealName, selectedMealType, "${proteinValue.toInt()}g Protein")
                                mealName = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AlayaLavender),
                        shape = RoundedCornerShape(16.dp)
                    ) { Text("ADD TO PLAN", fontWeight = FontWeight.Bold, color = Color.White) }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text("TODAY'S SCHEDULE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)

            addedMeals.forEach { item ->
                AddedMealCard(item, AlayaLavender, LavenderLight, onDelete = { vm.deleteMeal(item.id) })
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AddedMealCard(item: LocalMealItem, themeColor: Color, lightColor: Color, onDelete: () -> Unit) {
    Surface(color = Color.White, shape = RoundedCornerShape(20.dp), border = BorderStroke(1.dp, lightColor)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(lightColor), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Restaurant, null, tint = themeColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(item.type, fontSize = 12.sp, color = Color.Gray)
            }
            Text(item.protein, fontWeight = FontWeight.Black, fontSize = 12.sp, color = themeColor, modifier = Modifier.padding(end = 8.dp))
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color.Red.copy(0.6f), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun PopularMealCard(name: String, imageRes: Int) {
    Surface(
        modifier = Modifier.size(140.dp, 175.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                contentScale = ContentScale.Crop
            )
            Text(name, modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun SelectionGrid(items: List<String>, selected: List<String>, onSelect: (String) -> Unit, color: Color) {
    Column {
        items.chunked(3).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { item ->
                    Row(modifier = Modifier.weight(1f).clickable { onSelect(item) }, verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selected.contains(item),
                            onCheckedChange = { onSelect(item) },
                            colors = CheckboxDefaults.colors(checkedColor = color),
                            modifier = Modifier.scale(0.8f)
                        )
                        Text(item, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}