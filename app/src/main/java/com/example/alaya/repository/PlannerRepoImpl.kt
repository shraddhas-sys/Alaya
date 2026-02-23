package com.example.alaya.repository

import com.google.firebase.database.FirebaseDatabase
import com.example.alaya.model.YogaPlanModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PlannerRepoImpl (
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) : PlannerRepo {

    private val dbRef = db.getReference("yoga_plans")

    override suspend fun addPlan(plan: YogaPlanModel, callback: (Boolean, String?) -> Unit) {
        // Create a unique ID for the plan under the user's ID
        val newPlanRef = dbRef.child(plan.userId).push()
        val planWithId = plan.copy(id = newPlanRef.key ?: "")

        newPlanRef.setValue(planWithId)
            .addOnSuccessListener { callback(true, "Plan Added") }
            .addOnFailureListener { callback(false, it.message) }
    }

    override fun getPlans(userId: String): Flow<List<YogaPlanModel>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull {
                    it.getValue(YogaPlanModel::class.java)
                }
                trySend(items)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        dbRef.child(userId).addValueEventListener(listener)
        awaitClose { dbRef.child(userId).removeEventListener(listener) }
    }
}