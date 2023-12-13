package com.example.musicapp.module

import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class ViewModelModule {
    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: MusicViewModelFactory?): ViewModelProvider.Factory?
}