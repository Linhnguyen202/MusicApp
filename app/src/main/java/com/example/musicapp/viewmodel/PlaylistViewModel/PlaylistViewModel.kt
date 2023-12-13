package com.example.musicapp.viewmodel.PlaylistViewModel

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
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(val app : MyApplication, val musicRepository: MusicRepository) : ViewModel() {
    val addPlaylist : MutableLiveData<Resource<PlaylistResponse>> = MutableLiveData()
    val getPlaylist : MutableLiveData<Resource<UserPlaylistResponse>> = MutableLiveData()
    val addMusicToPlaylist : MutableLiveData<Resource<AddPlaylistResponse>> = MutableLiveData()
    val deletePlaylist : MutableLiveData<Resource<DeletePlaylistResponse>> = MutableLiveData()
    val updateNamePlaylist : MutableLiveData<Resource<UpdatePlaylistResponse>> = MutableLiveData()

    val playlistData : MutableLiveData<Resource<PlaylistDataResponse>> = MutableLiveData()

    val deleteMusicData : MutableLiveData<Resource<DeleteMusicResponse>> = MutableLiveData()

    public fun addUserPlaylist(playlistBody: PlaylistBody, header: String) =viewModelScope.launch(Dispatchers.IO) {
        addPlaylist.postValue(Resource.Loading())
        val response = musicRepository.addPlaylist(playlistBody, header)
        addPlaylist.postValue(handlePlaylistResponse(response))
    }
    public fun getUserPlaylist(header: String) =viewModelScope.launch(Dispatchers.IO) {
        getPlaylist.postValue(Resource.Loading())
        val response = musicRepository.getPlaylist(header)
        getPlaylist.postValue(handlePlaylistResponse(response))
    }

    public fun addMusicUserToPlaylist(addPlaylistBody: AddPlaylistBody, header: String) =viewModelScope.launch(Dispatchers.IO) {
        addMusicToPlaylist.postValue(Resource.Loading())
        val response = musicRepository.addMusicToPlaylist(addPlaylistBody, header)
        addMusicToPlaylist.postValue(handlePlaylistResponse(response))
    }

    public fun deleteUserPlaylist(id: String, header: String) =viewModelScope.launch(Dispatchers.IO) {
        deletePlaylist.postValue(Resource.Loading())
        val response = musicRepository.deletePlaylist(id, header)
        deletePlaylist.postValue(handlePlaylistResponse(response))
    }

    public fun updateNamePlaylist(editPlaylistBody: EditPlaylistBody, header: String) =viewModelScope.launch(Dispatchers.IO) {
        updateNamePlaylist.postValue(Resource.Loading())
        val response = musicRepository.updateNamePlaylist(editPlaylistBody, header)
        updateNamePlaylist.postValue(handlePlaylistResponse(response))
    }


    public fun getPlaylistDataList(id: String, header: String) =viewModelScope.launch(Dispatchers.IO) {
        playlistData.postValue(Resource.Loading())
        val response = musicRepository.getPlaylistData(id, header)
        playlistData.postValue(handlePlaylistResponse(response))
    }

    public fun deleteMusic(id: String,id_music: String, header: String) =viewModelScope.launch(Dispatchers.IO) {
        deleteMusicData.postValue(Resource.Loading())
        val response = musicRepository.deleteMusic(id,id_music, header)
        deleteMusicData.postValue(handlePlaylistResponse(response))
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