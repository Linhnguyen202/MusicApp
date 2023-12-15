package com.example.musicapp.viewmodel.PlaylistViewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.application.MyApplication
import com.example.musicapp.model.*
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(val app : MyApplication, val musicRepository: MusicRepository) : AndroidViewModel(app) {
    val addPlaylist : MutableLiveData<Resource<PlaylistResponse>> = MutableLiveData()
    val getPlaylist : MutableLiveData<Resource<UserPlaylistResponse>> = MutableLiveData()
    val addMusicToPlaylist : MutableLiveData<Resource<AddPlaylistResponse>> = MutableLiveData()
    val deletePlaylist : MutableLiveData<Resource<DeletePlaylistResponse>> = MutableLiveData()
    val updateNamePlaylist : MutableLiveData<Resource<UpdatePlaylistResponse>> = MutableLiveData()

    val playlistData : MutableLiveData<Resource<PlaylistDataResponse>> = MutableLiveData()

    val deleteMusicData : MutableLiveData<Resource<DeleteMusicResponse>> = MutableLiveData()

    public fun addUserPlaylist(playlistBody: PlaylistBody, header: String) =viewModelScope.launch(Dispatchers.IO) {
        addPlaylist.postValue(Resource.Loading())
        try{
            val response = musicRepository.addPlaylist(playlistBody, header)
            addPlaylist.postValue(handlePlaylistResponse(response))
        }
        catch (e : IOException){
            addPlaylist.postValue(Resource.Error(e.message.toString()))
        }


    }
    public fun getUserPlaylist(header: String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            getPlaylist.postValue(Resource.Loading())
            val response = musicRepository.getPlaylist(header)
            getPlaylist.postValue(handlePlaylistResponse(response))
        }
        catch (e : IOException){
            getPlaylist.postValue(Resource.Error(e.message.toString()))
        }


    }

    public fun addMusicUserToPlaylist(addPlaylistBody: AddPlaylistBody, header: String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            addMusicToPlaylist.postValue(Resource.Loading())
            val response = musicRepository.addMusicToPlaylist(addPlaylistBody, header)
            addMusicToPlaylist.postValue(handlePlaylistResponse(response))
        }
        catch (e : IOException){
            addMusicToPlaylist.postValue(Resource.Error(e.message.toString()))
        }

    }

    public fun deleteUserPlaylist(id: String, header: String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            deletePlaylist.postValue(Resource.Loading())
            val response = musicRepository.deletePlaylist(id, header)
            deletePlaylist.postValue(handlePlaylistResponse(response))
        }
        catch (e : IOException){
            deletePlaylist.postValue(Resource.Error(e.message.toString()))
        }
    }

    public fun updateNamePlaylist(editPlaylistBody: EditPlaylistBody, header: String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            updateNamePlaylist.postValue(Resource.Loading())
            val response = musicRepository.updateNamePlaylist(editPlaylistBody, header)
            updateNamePlaylist.postValue(handlePlaylistResponse(response))
        }
        catch (e : IOException){
            deletePlaylist.postValue(Resource.Error(e.message.toString()))
        }

    }


    public fun getPlaylistDataList(id: String, header: String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            playlistData.postValue(Resource.Loading())
            val response = musicRepository.getPlaylistData(id, header)
            playlistData.postValue(handlePlaylistResponse(response))
        }
        catch (e : IOException){
            playlistData.postValue(Resource.Error(e.toString()))
        }

    }

    public fun deleteMusic(id: String,id_music: String, header: String) =viewModelScope.launch(Dispatchers.IO) {
        try{
            deleteMusicData.postValue(Resource.Loading())
            val response = musicRepository.deleteMusic(id,id_music, header)
            deleteMusicData.postValue(handlePlaylistResponse(response))
        }
        catch (e : IOException){
            deleteMusicData.postValue(Resource.Error(e.toString()))
        }

    }

    private fun<T>handlePlaylistResponse(response : Response<T>) : Resource<T>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}