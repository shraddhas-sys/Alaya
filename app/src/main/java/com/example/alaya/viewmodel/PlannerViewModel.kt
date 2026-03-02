package com.example.alaya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alaya.model.YogaPlanModel
import com.example.alaya.repository.PlannerRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PlannerViewModel (private val repo: PlannerRepo) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid ?: ""
    private val dbRef = FirebaseDatabase.getInstance().getReference()

    private val _plans = MutableStateFlow<List<YogaPlanModel>>(emptyList())
    val plans: StateFlow<List<YogaPlanModel>> = _plans

    init {
        if (userId.isNotEmpty()) {
            fetchPlans()
        }
    }

    private fun fetchPlans() {
        viewModelScope.launch {
            repo.getPlans(userId).collect { fetchedPlans ->
                _plans.value = fetchedPlans
            }
        }
    }

    fun savePlan(date: String, type: String, duration: String, notes: String) {
        if (userId.isEmpty()) return

        val newPlan = YogaPlanModel(
            id = "",
            userId = userId,
            date = date,
            yogaType = type,
            duration = duration,
            notes = notes
        )

        viewModelScope.launch {
            repo.addPlan(newPlan) { success, msg ->
                if (success) {
                    updateUserProgressOnPlan()

                    sendNotification(
                        title = "Yoga Plan Added!!️",
                        message = "New $type session scheduled for $date ($duration)."
                    )
                }
            }
        }
    }

    private fun updateUserProgressOnPlan() {
        val userRef = dbRef.child("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentCount = snapshot.child("completedCount").getValue(Int::class.java) ?: 0
                val currentStreak = snapshot.child("streak").getValue(Int::class.java) ?: 0

                val updates = mapOf(
                    "completedCount" to currentCount + 1,
                    "streak" to currentStreak + 1
                )
                userRef.updateChildren(updates)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun deletePlan(plan: YogaPlanModel) {
        if (userId.isEmpty()) return

        viewModelScope.launch {
            repo.deletePlan(userId, plan.id) { success, msg ->
                if (success) {
                    sendNotification(
                        title = "Session Cancelled ",
                        message = "${plan.yogaType} session for ${plan.date} has been removed."
                    )
                }
            }
        }
    }

    private fun sendNotification(title: String, message: String) {
        if (userId.isEmpty()) return
        viewModelScope.launch {
            try {
                val notifRef = dbRef.child("notifications").push()
                val notificationData = mapOf(
                    "id" to (notifRef.key ?: ""),
                    "title" to title,
                    "message" to message,
                    "userId" to userId,
                    "timestamp" to System.currentTimeMillis(),
                    "isRead" to false
                )
                notifRef.setValue(notificationData).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}