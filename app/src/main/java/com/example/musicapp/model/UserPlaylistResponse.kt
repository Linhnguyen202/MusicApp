package com.example.musicapp.model

data class UserPlaylistResponse(
    val pagination: PaginationRes,
    val data : List<Playlist>
)
