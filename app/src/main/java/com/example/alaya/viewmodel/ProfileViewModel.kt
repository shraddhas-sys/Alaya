package com.example.alaya.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    var userName by mutableStateOf("Loading...")
    var userEmail by mutableStateOf("Loading...")

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            dbRef.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    userName = snapshot.child("name").value?.toString() ?: "No Name"
                    userEmail = snapshot.child("email").value?.toString() ?: "No Email"
                } else {
                    userName = "Profile Empty"
                    userEmail = "No Email Found"
                }
            }.addOnFailureListener {
                it.printStackTrace()
                userName = "Connection Error"
            }
        } else {
            userName = "Guest User"
            userEmail = "Not Logged In"
        }
    }

    fun logout(onLogout: () -> Unit) {
        auth.signOut()
        userName = "Logged Out"
        userEmail = ""
        onLogout()
    }
}