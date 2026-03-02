package com.example.alaya.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alaya.model.YogaPlanModel
import com.example.alaya.repository.PlannerRepoImpl
import com.example.alaya.ui.theme.AlayaDeepPurple
import com.example.alaya.ui.theme.AlayaNudeCream
import com.example.alaya.view.ui.theme.AlayaTheme
import com.example.alaya.viewmodel.DashboardViewModel
import com.example.alaya.viewmodel.PlannerViewModel
import com.example.alaya.viewmodel.PlannerViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
fun PlannerScreen(
    navController: NavHostController? = null,
    onBack: () -> Unit,
    viewModel: PlannerViewModel = viewModel(
        factory = PlannerViewModelFactory(PlannerRepoImpl())
    ),
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    // Collect plans from ViewModel
    val savedPlans by viewModel.plans.collectAsState()
    val context = LocalContext.current

    var date by remember { mutableStateOf("") }
    var yogaType by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Time picker
    var selectedHour by remember { mutableStateOf("0") }
    var selectedMin by remember { mutableStateOf("30") }
    var selectedSec by remember { mutableStateOf("0") }

    // Date picker and dropdown visibility states
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    var typeExpanded by remember { mutableStateOf(false) }
    val yogaOptions = listOf("Vinyasa", "Hatha", "Yin Yoga", "Ashtanga", "Restorative")
    val filteredOptions = yogaOptions.filter { it.contains(yogaType, ignoreCase = true) }

    Scaffold(
        containerColor = AlayaNudeCream,
        topBar = {
            TopAppBar(
                title = { Text("Yoga Planner", fontWeight = FontWeight.ExtraBold, color = AlayaDeepPurple) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AlayaDeepPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
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
            Text("NEW PLAN", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AlayaDeepPurple.copy(alpha = 0.6f))
            Text("Schedule a session", fontSize = 24.sp, fontWeight = FontWeight.Black, color = AlayaDeepPurple)
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(28.dp),
                shadowElevation = 0.5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {

                   // date section
                    Column {
                        Text("DATE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, AlayaDeepPurple.copy(alpha = 0.15f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AlayaDeepPurple)
                        ) {
                            Text(date.ifEmpty { "Select Date" }, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.weight(1f))
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = AlayaDeepPurple)
                        }
                    }

                  // yoga type section
                    Column {
                        Text("YOGA TYPE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        ExposedDropdownMenuBox(
                            expanded = typeExpanded,
                            onExpandedChange = { typeExpanded = !typeExpanded }
                        ) {
                            TextField(
                                value = yogaType,
                                onValueChange = { yogaType = it; typeExpanded = true },
                                placeholder = { Text("Select style...", fontSize = 14.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = AlayaDeepPurple,
                                    unfocusedIndicatorColor = AlayaDeepPurple.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            if (filteredOptions.isNotEmpty()) {
                                ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                                    filteredOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option, fontWeight = FontWeight.Medium) },
                                            onClick = { yogaType = option; typeExpanded = false }
                                        )
                                    }
                                }
                            }
                        }
                    }

                   // duration section
                    Column {
                        Text("DURATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            EditableTimeDropdown(label = "Hrs", options = (0..5).map { it.toString() }, value = selectedHour, onValueChange = { selectedHour = it }, modifier = Modifier.weight(1f))
                            EditableTimeDropdown(label = "Min", options = listOf("0", "15", "30", "45"), value = selectedMin, onValueChange = { selectedMin = it }, modifier = Modifier.weight(1f))
                            EditableTimeDropdown(label = "Sec", options = listOf("0", "30"), value = selectedSec, onValueChange = { selectedSec = it }, modifier = Modifier.weight(1f))
                        }
                    }

                    PlannerInput(label = "NOTES (OPTIONAL)", value = notes, onValueChange = { notes = it }, placeholder = "Focus on breathing...")

                    // notification logic part for planned session
                    Button(
                        onClick = {
                            if (date.isNotEmpty() && yogaType.isNotEmpty()) {
                                val finalDuration = "${selectedHour}h ${selectedMin}m ${selectedSec}s"
                                viewModel.savePlan(date, yogaType, finalDuration, notes)
                                dashboardViewModel.sendNotification(
                                    title = "New Yoga Session Planned! ️",
                                    message = "You have scheduled $yogaType for $date."
                                )

                                Toast.makeText(context, "Session Scheduled! ", Toast.LENGTH_SHORT).show()

                                date = ""; yogaType = ""; notes = ""; selectedHour = "0"; selectedMin = "30"; selectedSec = "0"
                            } else {
                                Toast.makeText(context, "Please fill date and yoga type!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AlayaDeepPurple)
                    ) {
                        Text("ADD TO PLANNER", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(4.dp, 16.dp).clip(CircleShape).background(AlayaDeepPurple))
                Spacer(modifier = Modifier.width(8.dp))
                Text("UPCOMING SESSIONS", fontSize = 14.sp, fontWeight = FontWeight.Black, color = AlayaDeepPurple)
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (savedPlans.isEmpty()) {
                Text("Your schedule is empty.", modifier = Modifier.padding(vertical = 20.dp), color = Color.Gray, fontSize = 14.sp)
            } else {
                savedPlans.forEach { plan ->
                    DateCard(
                        plan = plan,
                        modifier = Modifier.fillMaxWidth(),
                        onDelete = { planToDelete -> viewModel.deletePlan(planToDelete) },
                        onComplete = { completedPlan ->
                            dashboardViewModel.onPlanComplete(completedPlan.yogaType)

                            viewModel.deletePlan(completedPlan)

                            Toast.makeText(context, "Great job! Plan completed ✅", Toast.LENGTH_SHORT).show()
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                    } ?: ""
                    date = selectedDate
                    showDatePicker = false
                }) { Text("OK", color = AlayaDeepPurple, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = Color.Gray) } }
        ) { DatePicker(state = datePickerState) }
    }
}

@Composable
fun DateCard(
    plan: YogaPlanModel,
    modifier: Modifier,
    onDelete: (YogaPlanModel) -> Unit,
    onComplete: (YogaPlanModel) -> Unit
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 0.5.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(plan.date, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                Text(plan.yogaType, fontSize = 18.sp, fontWeight = FontWeight.Black, color = AlayaDeepPurple)
                if (plan.notes.isNotEmpty()) {
                    Text(plan.notes, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
               // success tick
                IconButton(onClick = { onComplete(plan) }) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Complete",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text(plan.duration, fontSize = 12.sp, color = AlayaDeepPurple, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { onDelete(plan) }, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.4f), modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

// dropdown for picking time units
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableTimeDropdown(label: String, options: List<String>, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = value,
                onValueChange = { onValueChange(it) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = AlayaDeepPurple,
                    unfocusedIndicatorColor = AlayaDeepPurple.copy(alpha = 0.1f)
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AlayaDeepPurple),
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { onValueChange(option); expanded = false })
                }
            }
        }
    }
}

@Composable
fun PlannerInput(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 14.sp, color = Color.Gray.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = AlayaDeepPurple,
                unfocusedIndicatorColor = AlayaDeepPurple.copy(alpha = 0.1f)
            ),
            singleLine = true
        )
    }
}