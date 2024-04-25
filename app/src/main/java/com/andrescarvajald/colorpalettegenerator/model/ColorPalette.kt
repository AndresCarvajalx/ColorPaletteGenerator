package com.andrescarvajald.colorpalettegenerator.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.andrescarvajald.colorpalettegenerator.util.Converters

data class ColorPalette(
    var hexCode: Long,
    var locked: Boolean = false,
)

@Entity
data class ColorPaletteEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @TypeConverters(Converters::class) @ColumnInfo(name = "colors") val palettes: List<ColorPalette>
)
