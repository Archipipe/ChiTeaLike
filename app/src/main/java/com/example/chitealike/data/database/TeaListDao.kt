package com.example.chitealike.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TeaListDao {
    @Query("SELECT * FROM teas")
    suspend fun getTeaList(): List<TeaDBModel>

    @Query("SELECT * FROM teas WHERE tea_id = :teaItemId")
    suspend fun getTeaItem(teaItemId: Int): TeaDBModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teas: List<TeaDBModel>)

}