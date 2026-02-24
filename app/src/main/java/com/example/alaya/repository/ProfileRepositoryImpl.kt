package com.example.alaya.repository

import com.example.alaya.model.User
import com.google.firebase.database.FirebaseDatabase

class ProfileRepositoryImpl : ProfileRepository {
    private val db = FirebaseDatabase.getInstance().getReference("users")

    override fun getUserProfile(uid: String, onResult: (User?) -> Unit) {
        db.child(uid).get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(User::class.java)
            onResult(user)
        }.addOnFailureListener {
            onResult(null)
        }
    }

    override fun updateUsername(uid: String, newName: String, onComplete: (Boolean) -> Unit) {
        db.child(uid).child("name").setValue(newName)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }
}