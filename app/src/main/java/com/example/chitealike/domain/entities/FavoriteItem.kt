package com.example.chitealike.domain.entities

data class FavoriteItem (
    val id: Int,
    val teaId: Int?,
    val name: String,
    val description: String
)