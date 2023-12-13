package com.example.musicapp.viewmodel.MusicViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.application.MyApplication
import com.example.musicapp.model.MusicResponse
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class MusicViewModel @Inject constructor(val app : MyApplication,val musicRepository: MusicRepository) :ViewModel() {
   val topViewMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val trendingMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val newMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val favoriteMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()
    val searchMusic : MutableLiveData<Resource<MusicResponse>> = MutableLiveData()

    public fun getMusic(type : String) =viewModelScope.launch(Dispatchers.IO) {
        topViewMusic.postValue(Resource.Loading())
        val response = musicRepository.getMusic("new-music")
        topViewMusic.postValue(handleMusicResponse(response))
    }
    public fun getTrending(type : String) =viewModelScope.launch(Dispatchers.IO) {
        trendingMusic.postValue(Resource.Loading())
        val response = musicRepository.getMusic(type)
        trendingMusic.postValue(handleMusicResponse(response))
    }
    public fun getNew(type : String) =viewModelScope.launch(Dispatchers.IO){
        newMusic.postValue(Resource.Loading())
        val response = musicRepository.getMusic(type)
        newMusic.postValue(handleMusicResponse(response))
    }
    public fun getFavorite(type : String) =viewModelScope.launch(Dispatchers.IO) {
        favoriteMusic.postValue(Resource.Loading())
        val response = musicRepository.getMusic("new-music")
        favoriteMusic.postValue(handleMusicResponse(response))
    }

    public fun getSearching(query: String) = viewModelScope.launch(Dispatchers.IO) {
        searchMusic.postValue(Resource.Loading())
        val response = musicRepository.searchMusic(query)
        searchMusic.postValue(handleMusicResponse(response))
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