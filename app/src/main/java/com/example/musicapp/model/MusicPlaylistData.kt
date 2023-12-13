package com.example.musicapp.model

data class MusicPlaylistData (
    val createdAt : String,
    val updatedAt : String,
    val _id : String,
    val id_account : String,
    val id_music : String,
    val music: Music,
    val id_list : String,
    val __v : String
)