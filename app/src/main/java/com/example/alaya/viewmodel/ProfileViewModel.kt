package com.example.alaya.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alaya.repository.ProfileRepository
import com.example.alaya.repository.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepositoryImpl()
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var userName by mutableStateOf("Loading...")
    var userEmail by mutableStateOf("Loading...")

    init { fetchUserProfile() }

    fun fetchUserProfile() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            repository.getUserProfile(uid) { user ->
                userName = user?.name ?: "No Name"
                userEmail = user?.email ?: "No Email Found"
            }
        }
    }

    fun updateName(newName: String) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.updateUsername(uid, newName).onSuccess {
                userName = newName
            }
        }
    }

    fun updatePassword(newPwd: String) {
        viewModelScope.launch {
            repository.updatePassword(newPwd)
        }
    }
    fun deleteAccount(onDeleted: () -> Unit) {
        val user = auth.currentUser
        val uid = user?.uid ?: return

        viewModelScope.launch {
            repository.deleteUserFromDb(uid).onSuccess {
                user.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onDeleted()
                    }
                }
            }.onFailure {
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        repository.logout()
        onLogout()
    }
}