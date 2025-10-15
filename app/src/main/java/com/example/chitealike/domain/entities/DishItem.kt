package com.example.chitealike.domain.entities

import com.example.chitealike.domain.enums.TeaType

data class DishItem(
    val id: Int,
    val name: String,
    val type: String,
    val dishImageFileName: String? = null,
    val description: String,

)