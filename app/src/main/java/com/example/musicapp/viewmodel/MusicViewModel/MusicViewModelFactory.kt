package com.example.musicapp.viewmodel.MusicViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.application.MyApplication
import com.example.musicapp.repository.MusicRepository
import javax.inject.Inject

class MusicViewModelFactory @Inject constructor(val app : MyApplication, val musicRepository: MusicRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MusicViewModel(app,musicRepository) as T
    }
}