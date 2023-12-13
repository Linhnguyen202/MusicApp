package com.example.musicapp.viewmodel.AuthViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.application.MyApplication
import com.example.musicapp.model.RegisterBody
import com.example.musicapp.model.UserResponse
import com.example.musicapp.model.loginBody
import com.example.musicapp.repository.AuthRepository
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class AuthViewModel @Inject constructor(val app : MyApplication, val authRepository: AuthRepository) : ViewModel() {
    var loginData : MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    val registerData : MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    public fun loginUser(body: loginBody) =viewModelScope.launch(Dispatchers.IO) {
        loginData.postValue(Resource.Loading())
        val response = authRepository.loginUser(body)
        loginData.postValue(handleMusicResponse(response))
    }
    public fun registerUser(body: RegisterBody) =viewModelScope.launch(Dispatchers.IO) {
        registerData.postValue(Resource.Loading())
        val response = authRepository.registerUser(body)
        registerData.postValue(handleMusicResponse(response))
    }
    private fun<T>handleMusicResponse(response : Response<T>) : Resource<T>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.code().toString())
    }
}