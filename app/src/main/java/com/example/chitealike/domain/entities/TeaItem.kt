package com.example.chitealike.domain.entities

import com.example.chitealike.domain.enums.TeaType

data class TeaItem(
    val id: Int,
    val name: String,
    val type: TeaType,
    val imageFileName: String?,
    val country: String,
    val taste: List<String>,
    val description: String,
    val classicTime: String,
    val classicTemperature: String,
    val classicTeaAmount: String,
    val classicBrewingAmount: String,
    val brewingTime: String,
    val brewingTemperature: String,
    val brewingTeaAmount: String,
    val brewingBrewingAmount: String,
)