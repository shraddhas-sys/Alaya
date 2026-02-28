package com.example.alaya.model
data class AlayaNotification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val userId: String = "",
    val timestamp: Long = 0L,
    val isRead: Boolean = false,
    val type: String = "",
    val timeAgo: String = ""
)
