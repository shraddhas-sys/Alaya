package com.example.alaya.repository

import com.example.alaya.model.LocalMealItem
import com.google.firebase.auth.FirebaseAuth
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
    private val notifDatabase = FirebaseDatabase.getInstance().getReference("notifications")
    private val auth = FirebaseAuth.getInstance()

    override fun getMeals(): Flow<List<LocalMealItem>> = callbackFlow {
        val uid = auth.currentUser?.uid ?: ""
        if (uid.isEmpty()) {
            trySend(emptyList())
        } else {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.mapNotNull { doc ->
                        LocalMealItem(
                            id = doc.key ?: "",
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
            database.child(uid).addValueEventListener(listener)
            awaitClose { database.child(uid).removeEventListener(listener) }
        }
    }

    override suspend fun addMeal(meal: LocalMealItem): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No User"))
            val key = database.child(uid).push().key ?: return Result.failure(Exception("No Key"))
            database.child(uid).child(key).setValue(meal.copy(id = key)).await()
            sendMealNotification(uid, meal.name, meal.type)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    private suspend fun sendMealNotification(uid: String, mealName: String, mealType: String) {
        val notifKey = notifDatabase.push().key ?: return
        val data = mapOf(
            "id" to notifKey, "title" to "Diet Plan Added! 🥗",
            "message" to "New $mealType: $mealName added.",
            "userId" to uid, "timestamp" to System.currentTimeMillis(), "isRead" to false
        )
        notifDatabase.child(notifKey).setValue(data).await()
    }

    override suspend fun deleteMeal(mealId: String): Result<Unit> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No User"))
        return try {
            database.child(uid).child(mealId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
}