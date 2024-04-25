package com.andrescarvajald.colorpalettegenerator.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andrescarvajald.colorpalettegenerator.domain.dao.ColorPaletteDao
import com.andrescarvajald.colorpalettegenerator.model.ColorPaletteEntity
import com.andrescarvajald.colorpalettegenerator.util.Converters

@Database(entities = [ColorPaletteEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ColorPaletteDatabase : RoomDatabase() {
    abstract val colorPaletteDao: ColorPaletteDao
}