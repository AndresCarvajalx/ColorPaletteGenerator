package com.andrescarvajald.colorpalettegenerator.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrescarvajald.colorpalettegenerator.model.ColorPaletteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorPaletteDao {
    @Query("SELECT * FROM colorpaletteentity")
    fun getPalette(): Flow<List<ColorPaletteEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPalette(palette: ColorPaletteEntity)
    @Delete
    suspend fun removePalette(palette: ColorPaletteEntity)
}