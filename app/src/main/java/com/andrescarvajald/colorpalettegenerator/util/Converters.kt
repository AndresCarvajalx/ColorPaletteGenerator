package com.andrescarvajald.colorpalettegenerator.util

import androidx.room.TypeConverter
import com.andrescarvajald.colorpalettegenerator.model.ColorPalette
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun jsonToList(value: String): List<ColorPalette> {
        return Gson().fromJson(value, Array<ColorPalette>::class.java).toList()
    }

    @TypeConverter
    fun listToJson(list: List<ColorPalette>): String = Gson().toJson(list)
}

/*
        @TypeConverter fun fromLong(value: Long): String = value.toString()
        @TypeConverter fun toLong(value: String): Long = value.toLong()
     */