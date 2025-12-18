package com.example.alaya.repository
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class AuthRepoImpl : AuthRepo {
    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    override fun register(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) onResult(true, null) else onResult(false, it.exception?.message)
        }
    }

    override fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) onResult(true, null) else onResult(false, it.exception?.message)
        }
    }

    override fun forgotPassword(
        email: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) onResult(true, null) else onResult(false, it.exception?.message)
        }
    }

    override fun saveUserData(
        userId: String,
        name: String,
        email: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val userMap = mapOf("userId" to userId, "name" to name, "email" to email)

        dbRef.child(userId).setValue(userMap).addOnCompleteListener {
            if (it.isSuccessful) onResult(true, null) else onResult(false, it.exception?.message)
        }
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser
}