package com.example.musicapp.repository

import com.example.musicapp.api.MusicApi
import com.example.musicapp.model.RegisterBody
import com.example.musicapp.model.loginBody
import javax.inject.Inject

class AuthRepository @Inject constructor(val api : MusicApi) {
    suspend fun loginUser(loginBody: loginBody) = api.loginUser(loginBody)

    suspend fun registerUser(registerBody: RegisterBody) = api.registerUser(registerBody)
}