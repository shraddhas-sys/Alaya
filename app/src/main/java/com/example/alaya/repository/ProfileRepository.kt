package com.example.alaya.repository

import com.example.alaya.model.User

interface ProfileRepository {
    fun getUserProfile(uid: String, onResult: (User?) -> Unit)
    fun updateUsername(uid: String, newName: String, onComplete: (Boolean) -> Unit)
}
