package com.example.alaya.repository

import com.example.alaya.model.YogaPlanModel
import kotlinx.coroutines.flow.Flow

interface PlannerRepo {
    suspend fun addPlan(plan: YogaPlanModel, callback: (Boolean, String?) -> Unit)
    fun getPlans(userId: String): Flow<List<YogaPlanModel>>
    suspend fun deletePlan(userId: String, planId: String, callback: (Boolean, String?) -> Unit)
}
