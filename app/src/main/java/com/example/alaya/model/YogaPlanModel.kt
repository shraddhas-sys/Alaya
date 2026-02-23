package com.example.alaya.model

data class YogaPlanModel(
    val id: String = "",        // Document ID from Firebase
    val userId: String = "",    // The UID of the user who created it
    val date: String = "",
    val yogaType: String = "",
    val duration: String = "",
    val notes: String = ""
)

