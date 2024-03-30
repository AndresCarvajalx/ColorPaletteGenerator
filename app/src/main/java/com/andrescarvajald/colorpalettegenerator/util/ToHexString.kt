package com.andrescarvajald.colorpalettegenerator.util

@OptIn(ExperimentalStdlibApi::class)
fun ToHexString(hex: Long) = hex.toHexString(
        HexFormat {
            upperCase = true
            number.prefix = ""
            number.removeLeadingZeros = true
        }
    )
