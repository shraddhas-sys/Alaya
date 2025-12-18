package com.example.alaya.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit)
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit)
    fun forgotPassword(email: String, onResult: (Boolean, String?) -> Unit)
    fun saveUserData(userId: String, name: String, email: String, onResult: (Boolean, String?) -> Unit)
    fun logout()
    fun getCurrentUser(): FirebaseUser?
}