package com.example.alaya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alaya.model.YogaPlanModel
import com.example.alaya.repository.PlannerRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlannerViewModel (private val repo: PlannerRepo) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid ?: ""

    private val _plans = MutableStateFlow<List<YogaPlanModel>>(emptyList())
    val plans: StateFlow<List<YogaPlanModel>> = _plans

    init {
        if (userId.isNotEmpty()) {
            fetchPlans()
        }
    }

    private fun fetchPlans() {
        viewModelScope.launch {
            repo.getPlans(userId).collect {
                _plans.value = it
            }
        }
    }

    fun savePlan(date: String, type: String, duration: String, notes: String) {
        if (userId.isEmpty()) return

        val newPlan = YogaPlanModel(
            userId = userId,
            date = date,
            yogaType = type,
            duration = duration,
            notes = notes
        )

        viewModelScope.launch {
            repo.addPlan(newPlan) { success, msg ->
                // Tapai le yaha success/error toast logic rakhna saknu huncha
            }
        }
    }
}
