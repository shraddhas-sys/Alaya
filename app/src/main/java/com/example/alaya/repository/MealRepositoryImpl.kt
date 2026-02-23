package com.example.alaya.repository
import com.example.alaya.model.LocalMealItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MealRepositoryImpl : MealRepository {
    private val database = FirebaseDatabase.getInstance().getReference("meal_plans")

    override fun getMeals(): Flow<List<LocalMealItem>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { doc ->
                    LocalMealItem(
                        id = doc.key ?: "", // Firebase ko key lai id ma halne
                        name = doc.child("name").value.toString(),
                        type = doc.child("type").value.toString(),
                        protein = doc.child("protein").value.toString()
                    )
                }
                trySend(items)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        database.addValueEventListener(listener)
        awaitClose { database.removeEventListener(listener) }
    }

    override suspend fun addMeal(meal: LocalMealItem): Result<Unit> {
        return try {
            val key = database.push().key ?: return Result.failure(Exception("Key generation failed"))
            // Data pathauda id pani pathaune taaki pachi delete garna milos
            database.child(key).setValue(meal.copy(id = key)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMeal(mealId: String): Result<Unit> {
        return try {
            if (mealId.isNotEmpty()) {
                database.child(mealId).removeValue().await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Invalid ID"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}