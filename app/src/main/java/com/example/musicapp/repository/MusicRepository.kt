package com.example.musicapp.repository

import com.example.musicapp.api.MusicApi
import com.example.musicapp.model.AddPlaylistBody
import com.example.musicapp.model.EditPlaylistBody
import com.example.musicapp.model.PlaylistBody
import javax.inject.Inject

class MusicRepository @Inject constructor(val api: MusicApi) {
    suspend fun getMusic(type : String) = api.getMusic(type)

    suspend fun getMusicDetail(id : String) = api.getMusicInfo(id)

    suspend fun searchMusic(query : String) = api.searchMusic(query)

    suspend fun addPlaylist (playlistBody: PlaylistBody, header: String) = api.makePlayList(playlistBody,header)

    suspend fun getPlaylist (header: String) = api.getPLaylist(header)

    suspend fun addMusicToPlaylist (addPlaylistBody : AddPlaylistBody, header: String) = api.addMusicToPlaylist(addPlaylistBody,header)

    suspend fun deletePlaylist(id: String, header : String) = api.deletePlaylist(id, header)

    suspend fun updateNamePlaylist(editPlaylistBody: EditPlaylistBody, token : String) = api.updateNamePlaylist(editPlaylistBody,token)

    suspend fun getPlaylistData(id: String, header : String) = api.getPlaylistData(id,header)

    suspend fun deleteMusic(id: String, music_id: String, header: String) = api.deleteMusic(id, music_id,header)
}