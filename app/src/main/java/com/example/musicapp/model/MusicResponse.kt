package com.example.musicapp.model

data class MusicResponse (
    val pagination : PaginationRes,
    val data : ArrayList<Music>
    )