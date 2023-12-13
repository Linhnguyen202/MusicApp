package com.example.musicapp.viewmodel.PlaylistViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.application.MyApplication
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModel
import javax.inject.Inject

class PlaylistViewModelFactory @Inject constructor(val app : MyApplication, val musicRepository: MusicRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaylistViewModel(app, musicRepository) as T
    }
}