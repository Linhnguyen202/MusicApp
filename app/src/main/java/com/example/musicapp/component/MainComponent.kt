package com.example.musicapp.component

import androidx.fragment.app.FragmentActivity
import com.example.musicapp.MainActivity
import com.example.musicapp.WebviewScreen
import com.example.musicapp.layout.*
import com.example.musicapp.module.MainModule
import com.example.musicapp.scope.ActivityScope

import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [MainModule::class])
public interface MainComponent {
    fun inject(activity: MainActivity)

    fun injectWebView(activity: WebviewScreen)
    fun injectHome(homeScreen: HomeScreen)

    fun injectSearch(searchScreen: SearchScreen)

    fun injectMenu(menuScreen: MenuScreen)

    fun injectProfile(profileScreen: ProfileScreen)

    fun injectMusic(musicScreen: MusicScreen)

    fun injectPlaylistSheet(playlistSheet: PlaylistSheet)

    fun injectPlaylistForm(playlistForm: PlaylistForm)

    fun injectPlaylistMusic(playlistListView: PlaylistListView)
    @Subcomponent.Factory
        interface Factory{
            fun create(@BindsInstance fragmentActivity: FragmentActivity) : MainComponent
        }

}