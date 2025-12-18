package com.example.alaya.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class DashboardViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _userName = mutableStateOf(auth.currentUser?.displayName ?: "Alaya")
    val userName: State<String> = _userName
    private val _selectedCategory = mutableStateOf("All")
    val selectedCategory: State<String> = _selectedCategory

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun logout(onLogout: () -> Unit) {
        auth.signOut()
        onLogout()
    }
}