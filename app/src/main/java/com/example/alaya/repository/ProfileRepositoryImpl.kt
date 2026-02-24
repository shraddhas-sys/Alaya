package com.example.alaya.repository

import com.example.alaya.model.User
import com.google.firebase.database.FirebaseDatabase

class ProfileRepositoryImpl : ProfileRepository {
    private val db = FirebaseDatabase.getInstance().getReference("users")

    override fun getUserProfile(uid: String, onResult: (User?) -> Unit) {
        db.child(uid).get().addOnSuccessListener { snapshot ->
            // Firebase le automatically JSON data lai User model ma convert garcha
            // Yesko lagi User model ma default values huna parcha (val name: String = "")
            val user = snapshot.getValue(User::class.java)
            onResult(user)
        }.addOnFailureListener {
            onResult(null)
        }
    }

    // Tapaiko update logic pani yaha hunu parcha
    override fun updateUsername(uid: String, newName: String, onComplete: (Boolean) -> Unit) {
        db.child(uid).child("name").setValue(newName)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }
}