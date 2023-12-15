package com.example.musicapp.api

import com.example.musicapp.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicApi {
    @GET("music/{type}")
    suspend fun getMusic(
        @Path("type") type : String
    ) : Response<MusicResponse>

    @GET("search")
    suspend fun searchMusic(
        @Query("query") query : String
    ) : Response<MusicResponse>

    @POST("account/login")
    suspend fun loginUser(
        @Body loginBody: loginBody
    )
    : Response<UserResponse>

    @POST("account/register")
    suspend fun registerUser(
        @Body registerBody: RegisterBody
    ) :  Response<UserResponse>

    @POST("list-music/create")
    suspend fun makePlayList(
        @Body playlistBody: PlaylistBody,
        @Header("Authorization") token: String,
    ) : Response<PlaylistResponse>


    @GET("list-music/get-list")
    suspend fun getPLaylist(
        @Header("Authorization") header: String
    ) : Response<UserPlaylistResponse>

    @PUT("list-music/add-list-music")
    suspend fun addMusicToPlaylist(
        @Body addPlaylistBody: AddPlaylistBody,
        @Header("Authorization") token: String
    ) : Response<AddPlaylistResponse>


    @DELETE("list-music/delete-list-music")
    suspend fun deletePlaylist(
        @Query("_id") id : String,
        @Header("Authorization") token: String
    ) : Response<DeletePlaylistResponse>

    @PUT("list-music/update-name-list-music")
    suspend fun updateNamePlaylist(
        @Body editPlaylistBody: EditPlaylistBody,
        @Header("Authorization") token: String
    ) : Response<UpdatePlaylistResponse>

    @GET("list-music/get-by-id")
    suspend fun getPlaylistData(
        @Query("_id") id : String,
        @Header("Authorization") token: String
    ) : Response<PlaylistDataResponse>

    @DELETE("list-music/delete-music")
    suspend fun deleteMusic(
        @Query("_id") id : String,
        @Query("_id_music") id_music : String,
        @Header("Authorization") token: String
    ) : Response<DeleteMusicResponse>

    @GET("music/get-by-id")
    suspend fun getMusicInfo(
        @Query("_id") id : String,
    ) : Response<MusicDetailResponse>

}