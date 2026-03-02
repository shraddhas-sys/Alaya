package com.example.alaya.repository

import com.example.alaya.model.UserProfile

interface ProfileRepository {
    fun getUserProfile(uid: String, onResult: (UserProfile?) -> Unit)

    suspend fun updateUsername(uid: String, newName: String): Result<Unit>
    suspend fun updateEmail(newEmail: String): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>

    suspend fun deleteUserFromDb(uid: String): Result<Unit>
    fun logout()
}