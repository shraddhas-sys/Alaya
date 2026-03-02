package com.example.alaya.repository

import com.example.alaya.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl : ProfileRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("users")

    override fun getUserProfile(uid: String, onResult: (UserProfile?) -> Unit) {
        db.child(uid).get().addOnSuccessListener { snapshot ->
            val profile = snapshot.getValue(UserProfile::class.java)
            onResult(profile)
        }.addOnFailureListener {
            onResult(null)
        }
    }

    override suspend fun updateUsername(uid: String, newName: String): Result<Unit> = try {
        db.child(uid).child("name").setValue(newName).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun updateEmail(newEmail: String): Result<Unit> = try {
        val user = auth.currentUser
        val uid = user?.uid ?: throw Exception("User not logged in")

        user.verifyBeforeUpdateEmail(newEmail).await()
        db.child(uid).child("email").setValue(newEmail).await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> = try {
        auth.currentUser?.updatePassword(newPassword)?.await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun deleteUserFromDb(uid: String): Result<Unit> = try {
        db.child(uid).removeValue().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun logout() {
        auth.signOut()
    }
}