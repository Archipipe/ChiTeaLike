package com.example.chitealike.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DishesListDao {

    @Query("select * from dishes")
    suspend fun getDishesList(): List<DishDBModel>

    @Query("select * from dishes where dish_id = :dishId")
    suspend fun getDishItem(dishId: Int): DishDBModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dishList: List<DishDBModel>)
}