package com.example.alaya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alaya.repository.PlannerRepo

@Suppress("UNCHECKED_CAST")
class PlannerViewModelFactory (
    private val repo: PlannerRepo
) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlannerViewModel::class.java)) {
                return PlannerViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
