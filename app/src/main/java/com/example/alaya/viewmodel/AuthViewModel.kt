package com.example.alaya.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alaya.model.UserModel
import com.example.alaya.repository.AuthRepo
import com.example.alaya.repository.AuthRepoImpl


class AuthViewModel : ViewModel() {
    private val repo: AuthRepo = AuthRepoImpl()

        fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
            if (email.isBlank() || pass.isBlank()) return onError("Empty fields")
            repo.login(email, pass) { success, error ->
                if (success) onSuccess() else onError(error ?: "Login Error")
            }
        }

        fun register(name: String, email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
            if (name.isBlank() || email.isBlank() || pass.isBlank()) return onResult(false, "Fill all fields")
            repo.register(email, pass) { success, error ->
                if (success) {
                    val uid = repo.getCurrentUser()?.uid ?: ""
                    repo.saveUserData(uid, name, email) { saved, saveError ->
                        onResult(saved, saveError)
                    }
                } else onResult(false, error)
            }
        }

        fun forgotPassword(email: String, onResult: (Boolean, String?) -> Unit) {
            if (email.isBlank()) return onResult(false, "Enter email")
            repo.forgotPassword(email, onResult)
        }
    }




