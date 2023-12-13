package com.example.musicapp.component

import android.app.Application
import com.example.musicapp.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun getMainCompent() : MainComponent.Factory

    fun getAuthComponent() : AuthComponent.Factory

    @Component.Builder
     interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}