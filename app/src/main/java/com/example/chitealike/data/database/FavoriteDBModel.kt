package com.example.chitealike.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorites",
    foreignKeys = [
        ForeignKey(
            entity = TeaDBModel::class,
            parentColumns = ["tea_id"],
            childColumns = ["tea_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
class FavoriteDBModel (
    @PrimaryKey(autoGenerate = true) val favorite_id: Int,
    val tea_id: Int?,
    val favorite_name: String,
    val favorite_description: String,
)