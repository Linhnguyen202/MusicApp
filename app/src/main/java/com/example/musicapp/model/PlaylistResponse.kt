package com.example.musicapp.model

data class PlaylistResponse(
    val image_list : String,
    val array_music : List<Music>,
    val createdAt : String,
    val updatedAt : String,
    val _id : String,
    val id_account : String,
    val name_list : String,
    val __v : String
)
