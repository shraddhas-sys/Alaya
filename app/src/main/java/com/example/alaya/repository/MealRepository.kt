package com.example.alaya.repository
import com.example.alaya.model.LocalMealItem
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    fun getMeals(): Flow<List<LocalMealItem>>
    suspend fun addMeal(meal: LocalMealItem): Result<Unit>
    suspend fun deleteMeal(mealId: String): Result<Unit>
}
