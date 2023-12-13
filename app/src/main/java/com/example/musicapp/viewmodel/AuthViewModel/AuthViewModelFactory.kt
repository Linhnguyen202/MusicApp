package com.example.musicapp.viewmodel.AuthViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.application.MyApplication
import com.example.musicapp.repository.AuthRepository
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModel
import javax.inject.Inject

class AuthViewModelFactory @Inject constructor(val app : MyApplication, val authRepository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(app, authRepository) as T
    }
}