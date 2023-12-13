package com.example.musicapp.component

import androidx.fragment.app.FragmentActivity
import com.example.musicapp.LoginSrcreen
import com.example.musicapp.MainActivity
import com.example.musicapp.RegisterScreen
import com.example.musicapp.module.AuthModule
import com.example.musicapp.scope.AuthScope
import dagger.BindsInstance
import dagger.Subcomponent

@AuthScope
@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {
    fun injectLoginScreen(activity: LoginSrcreen)

    fun injectRegisScreen(activity: RegisterScreen)
    @Subcomponent.Factory
    interface Factory{
        fun create() : AuthComponent
    }
}