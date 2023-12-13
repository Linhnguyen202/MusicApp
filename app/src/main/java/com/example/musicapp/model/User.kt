package com.example.musicapp.model

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val image: String,
    val password: String,
    val role: Int,
    val sum_comment: Any,
    val sum_list_music: Any,
    val sum_upload: Any,
    val updatedAt: String,
    val user_name: String
)