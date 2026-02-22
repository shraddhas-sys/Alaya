package com.example.alaya.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

class DashboardViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // --- User Info ---
    private val _userName = mutableStateOf(auth.currentUser?.displayName ?: "Alaya")
    val userName: State<String> = _userName

    // --- Yoga Progress States ---
    private val _currentActiveYoga = mutableStateOf<String?>(null)
    val currentActiveYoga: State<String?> = _currentActiveYoga

    private val _plannedMinutes = mutableStateOf(0)
    val plannedMinutes: State<Int> = _plannedMinutes

    private val _completedMinutes = mutableStateOf(0)
    val completedMinutes: State<Int> = _completedMinutes

    private val _scheduledPlansList = mutableStateListOf<Pair<String, Int>>()
    val scheduledPlansList: List<Pair<String, Int>> = _scheduledPlansList

    // --- NEW: Scheduled Plan States (For the Footer Plan Button) ---
    // This holds the plan created when navigating via the footer "Plan" icon
    private val _scheduledYogaType = mutableStateOf<String?>(null)
    val scheduledYogaType: State<String?> = _scheduledYogaType

    private val _scheduledTime = mutableStateOf(0)
    val scheduledTime: State<Int> = _scheduledTime

    // --- NEW: Meal Planning States ---
    private val _selectedMeal = mutableStateOf<String?>(null)
    val selectedMeal: State<String?> = _selectedMeal

    private val _plannedDiet = mutableStateOf<List<String>>(listOf())
    val plannedDiet: State<List<String>> = _plannedDiet

    // --- Actions ---

    /**
     * Called when the user confirms a plan (from Dialog OR Planning Screen).
     * This updates the state that the Dashboard observes.
     */
    fun updatePlan(yogaType: String, minutes: Int) {
        _currentActiveYoga.value = yogaType
        _plannedMinutes.value = minutes
        _completedMinutes.value = 0

        // Also update scheduled states to ensure consistency
        _scheduledYogaType.value = yogaType
        _scheduledTime.value = minutes
    }

    /**
     * Specifically for planning via the Footer "Plan" button.
     */
    fun setScheduledPlan(type: String, minutes: Int) {
        _scheduledYogaType.value = type
        _scheduledTime.value = minutes
        // Automatically makes this the active plan for the dashboard tracker
        _currentActiveYoga.value = type
        _plannedMinutes.value = minutes
    }

    /**
     * Adds a meal to the daily diet plan.
     */
    fun planMeal(mealName: String) {
        _selectedMeal.value = mealName
        if (!_plannedDiet.value.contains(mealName)) {
            val currentList = _plannedDiet.value.toMutableList()
            currentList.add(mealName)
            _plannedDiet.value = currentList
        }
    }

    fun completeSession() {
        _completedMinutes.value = _plannedMinutes.value
    }


    fun logout(onLogout: () -> Unit) {
        auth.signOut()
        onLogout()
    }
}