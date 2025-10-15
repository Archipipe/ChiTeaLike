package com.example.chitealike.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritesListDao {
    @Query("SELECT * FROM favorites")
    suspend fun getFavoritesList(): List<FavoriteDBModel>

    @Query("SELECT * FROM favorites WHERE favorite_id = :favoriteId")
    suspend fun getFavoriteItem(favoriteId: Int): FavoriteDBModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteItem(favoriteDBModel: FavoriteDBModel)

    @Query("DELETE FROM favorites WHERE favorite_id = :favoriteId")
    suspend fun deleteFavoriteItem(favoriteId: Int )
}