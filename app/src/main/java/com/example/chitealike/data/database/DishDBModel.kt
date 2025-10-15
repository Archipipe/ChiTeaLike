package com.example.chitealike.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes")
class DishDBModel (
    @PrimaryKey(autoGenerate = true) val dish_id: Int,
    val dish_name: String,
    val dish_type: String,
    val dish_image_file_name: String? = null,
    val dish_description: String,
)
