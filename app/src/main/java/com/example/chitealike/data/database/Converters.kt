package com.example.chitealike.data.database

import androidx.room.TypeConverter
import com.example.chitealike.domain.enums.TeaType
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromTeaType(value: TeaType): String = value.name

    @TypeConverter
    fun toTeaType(value:String): TeaType = TeaType.valueOf(value)

    @TypeConverter
    fun fromStringList(value: List<String>): String{
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>{
        return if (value.isNotEmpty()){
            Gson().fromJson(value, Array<String>::class.java).toList()
        } else {
            emptyList()
        }
    }
}