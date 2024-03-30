package com.andrescarvajald.colorpalettegenerator.domain.database

import com.andrescarvajald.colorpalettegenerator.model.ColorPalette
import java.util.concurrent.Flow

interface DatabaseRepository {
    fun getAllColor(): List<ColorPalette>
}