package com.example.chitealike.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chitealike.domain.enums.TeaType

@Entity(tableName = "teas")
class TeaDBModel(
    @PrimaryKey(autoGenerate = true) val tea_id: Int,
    val tea_name: String,
    val tea_type: TeaType,
    val tea_image_file_name: String? = null,
    val tea_country: String,
    val tea_taste: List<String>,
    val tea_description: String,
    val tea_classic_time: String,
    val tea_classic_temperature: String,
    val tea_classic_tea_amount: String,
    val tea_classic_brewing_amount: String,
    val tea_brewing_time: String,
    val tea_brewing_temperature: String,
    val tea_brewing_tea_amount: String,
    val tea_brewing_brewing_amount: String,

)