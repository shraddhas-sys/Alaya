package com.example.alaya.model
data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val completedCount: Int = 0,
    val streak: Int = 0,
    val totalGoal: Int = 20
)