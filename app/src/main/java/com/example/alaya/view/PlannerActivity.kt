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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveCircleOutline
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alaya.model.YogaPlanModel
import com.example.alaya.repository.PlannerRepoImpl
import com.example.alaya.ui.theme.RichCream
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
    )
) {
    val savedPlans by viewModel.plans.collectAsState()

    var date by remember { mutableStateOf("") }
    var yogaType by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // duration part
    var selectedHour by remember { mutableStateOf("0") }
    var selectedMin by remember { mutableStateOf("30") }
    var selectedSec by remember { mutableStateOf("0") }

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    var typeExpanded by remember { mutableStateOf(false) }
    val yogaOptions = listOf("Vinyasa", "Hatha", "Yin Yoga", "Ashtanga", "Restorative")
    val filteredOptions = yogaOptions.filter { it.contains(yogaType, ignoreCase = true) }


    Scaffold(
        containerColor = RichCream,
        topBar = {
            TopAppBar(
                title = { Text("Yoga Planner", fontWeight = FontWeight.Bold, color = AlayaPurplePrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AlayaPurplePrimary)
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
            Text("NEW PLAN", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaTextMuted)
            Text("Schedule a session", fontSize = 22.sp, fontWeight = FontWeight.Black, color = AlayaTextDark)
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {

                    Column {
                        Text("DATE", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaTextMuted)
                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, AlayaPurpleLight.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AlayaTextDark)
                        ) {
                            Text(date.ifEmpty { "Select Date" })
                            Spacer(Modifier.weight(1f))
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = AlayaPurplePrimary)
                        }
                    }

                    Column {
                        Text("YOGA TYPE", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaTextMuted)
                        ExposedDropdownMenuBox(
                            expanded = typeExpanded,
                            onExpandedChange = { typeExpanded = !typeExpanded }
                        ) {
                            TextField(
                                value = yogaType,
                                onValueChange = {
                                    yogaType = it
                                    typeExpanded = true
                                },
                                placeholder = { Text("Select or Type", fontSize = 14.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = AlayaPurplePrimary,
                                    unfocusedIndicatorColor = AlayaPurpleLight.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            if (filteredOptions.isNotEmpty()) {
                                ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                                    filteredOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                yogaType = option
                                                typeExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Column {
                        Text("DURATION", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaTextMuted)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            EditableTimeDropdown(label = "Hrs", options = (0..12).map { it.toString() }, value = selectedHour, onValueChange = { selectedHour = it }, modifier = Modifier.weight(1f))
                            EditableTimeDropdown(label = "Min", options = listOf("0", "15", "30", "45"), value = selectedMin, onValueChange = { selectedMin = it }, modifier = Modifier.weight(1f))
                            EditableTimeDropdown(label = "Sec", options = listOf("0", "30"), value = selectedSec, onValueChange = { selectedSec = it }, modifier = Modifier.weight(1f))
                        }
                    }

                  //  Notes part
                    PlannerInput(label = "NOTES (OPTIONAL)", value = notes, onValueChange = { notes = it }, placeholder = "e.g., Focus on hips")

                    Button(
                        onClick = {
                            if (date.isNotEmpty() && yogaType.isNotEmpty()) {
                                val finalDuration = "${selectedHour}h ${selectedMin}m ${selectedSec}s"
                                viewModel.savePlan(date, yogaType, finalDuration, notes)
                                // Reset fields
                                date = ""; yogaType = ""; notes = ""; selectedHour = "0"; selectedMin = "30"; selectedSec = "0"
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(58.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AlayaPurplePrimary)
                    ) {
                        Text("ADD TO PLANNER", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))
            Text("UPCOMING SESSIONS", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AlayaTextMuted)
            Spacer(modifier = Modifier.height(12.dp))

            if (savedPlans.isEmpty()) {
                Text("No sessions planned yet.", modifier = Modifier.padding(vertical = 20.dp), color = AlayaTextMuted)
            } else {
                savedPlans.forEach { plan ->
                    DateCard(plan = plan, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // the dates are shown to pick a date picker
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
                }) { Text("OK", color = AlayaPurplePrimary) }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = datePickerState) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableTimeDropdown(
    label: String,
    options: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(label, fontSize = 10.sp, color = AlayaTextMuted, fontWeight = FontWeight.Bold)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = value,
                onValueChange = { onValueChange(it) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = AlayaPurplePrimary,
                    unfocusedIndicatorColor = AlayaPurpleLight.copy(alpha = 0.3f)
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlannerInput(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
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
                unfocusedIndicatorColor = AlayaPurpleLight.copy(alpha = 0.5f)
            ),
            singleLine = true
        )
    }
}

@Composable
fun DateCard(plan: YogaPlanModel, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(plan.date, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AlayaTextDark)
                Text(plan.duration, fontSize = 12.sp, color = AlayaPurplePrimary, fontWeight = FontWeight.Bold)
            }
            Text(plan.yogaType, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = AlayaPurplePrimary)
            if (plan.notes.isNotEmpty()) {
                Text(plan.notes, fontSize = 11.sp, color = AlayaTextMuted)
            }
        }
    }
}