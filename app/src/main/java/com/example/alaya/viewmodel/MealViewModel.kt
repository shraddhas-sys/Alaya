package com.example.alaya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.alaya.model.LocalMealItem
import com.example.alaya.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MealViewModel(private val repo: MealRepository) : ViewModel() {
    val meals: Flow<List<LocalMealItem>> = repo.getMeals()

    fun addMeal(name: String, type: String, protein: String) {
        viewModelScope.launch {
            repo.addMeal(LocalMealItem(name = name, type = type, protein = protein))
        }
    }

    fun deleteMeal(id: String) {
        viewModelScope.launch {
            repo.deleteMeal(id)
        }
    }
}

class MealViewModelFactory(private val repo: MealRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
