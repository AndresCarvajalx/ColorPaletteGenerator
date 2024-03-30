package com.andrescarvajald.colorpalettegenerator.model

// import java.util.UUID

data class ColorPalette(
    // val id: UUID = UUID.randomUUID(),
    var hexCode: Long,
    var locked: Boolean = false,
)
