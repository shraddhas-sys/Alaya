package com.example.alaya.viewmodel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alaya.model.LocalMealItem
import com.example.alaya.repository.MealRepository
import com.example.alaya.repository.MealRepositoryImpl
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MealViewModel(
private val repository: MealRepository = MealRepositoryImpl()
) : ViewModel() {

    // Database bata real-time data stream garne
    val meals: StateFlow<List<LocalMealItem>> = repository.getMeals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addMeal(name: String, type: String, protein: String) {
        viewModelScope.launch {
            val newMeal = LocalMealItem(name = name, type = type, protein = protein)
            repository.addMeal(newMeal)
        }
    }

    fun deleteMeal(mealId: String) {
        viewModelScope.launch {
            repository.deleteMeal(mealId)
        }
    }
}