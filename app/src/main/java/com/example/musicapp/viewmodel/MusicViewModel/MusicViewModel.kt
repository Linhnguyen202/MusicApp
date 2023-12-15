package com.example.musicapp.viewmodel.MusicViewModel

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.application.MyApplication
import com.example.musicapp.model.MusicDetailResponse
import com.example.musicapp.model.MusicResponse
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.utils.NetworkConnectionInterceptor
import com.example.musicapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MusicViewModel @Inject constructor(val app : MyApplication,val musicRepository: MusicRepository) :AndroidViewModel(app) {
   val topViewMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val trendingMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val newMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val favoriteMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val searchMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()

    val musicDetail : MutableLiveData<Resource<MusicDetailResponse>> = MutableLiveData()
    public fun getMusic(type : String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            topViewMusic.postValue(Resource.Loading())
            val response = musicRepository.getMusic("new-music")
            topViewMusic.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            topViewMusic.postValue(Resource.Error(e.message.toString()))

        }

    }
    public fun getTrending(type : String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            trendingMusic.postValue(Resource.Loading())
            val response = musicRepository.getMusic(type)
            trendingMusic.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            trendingMusic.postValue(Resource.Error(e.message.toString()))

        }


    }
    public fun getNew(type : String) =viewModelScope.launch(Dispatchers.IO){
        try {
            newMusic.postValue(Resource.Loading())
            val response = musicRepository.getMusic(type)
            newMusic.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            newMusic.postValue(Resource.Error(e.message.toString()))
        }


    }
    public fun getFavorite(type : String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            favoriteMusic.postValue(Resource.Loading())
            val response = musicRepository.getMusic("new-music")
            favoriteMusic.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            favoriteMusic.postValue(Resource.Error(e.message.toString()))
        }


    }

    public fun getSearching(query: String) = viewModelScope.launch(Dispatchers.IO) {
        try{
            searchMusic.postValue(Resource.Loading())
            val response = musicRepository.searchMusic(query)
            searchMusic.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            searchMusic.postValue(Resource.Error(e.message.toString()))
        }


    }

    public fun getMusicDetail(id: String) = viewModelScope.launch(Dispatchers.IO) {
        try{
            musicDetail.postValue(Resource.Loading())
            val response = musicRepository.getMusicDetail(id)
            musicDetail.postValue(handleMusicResponse(response))
        }
        catch (e : IOException){
            musicDetail.postValue(Resource.Error(e.message.toString()))
        }

    }

    private fun<T>handleMusicResponse(response : Response<T>) : Resource<T>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}