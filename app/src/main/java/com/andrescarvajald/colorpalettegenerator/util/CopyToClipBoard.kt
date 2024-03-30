package com.andrescarvajald.colorpalettegenerator.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

fun <T, A> CopyToClipBoard(context: Context, toCopy: T, axis: A) {
    val clipBoard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipBoard.setPrimaryClip(
        ClipData.newPlainText(
            "",
            "${axis}${toCopy}"
        )
    )
}