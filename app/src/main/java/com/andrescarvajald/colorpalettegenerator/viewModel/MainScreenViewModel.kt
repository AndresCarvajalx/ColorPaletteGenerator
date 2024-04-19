package com.andrescarvajald.colorpalettegenerator.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrescarvajald.colorpalettegenerator.domain.dao.ColorPaletteDao
import com.andrescarvajald.colorpalettegenerator.model.ColorPalette
import com.andrescarvajald.colorpalettegenerator.model.ColorPaletteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainScreenViewModel(private val dao: ColorPaletteDao) : ViewModel() {
    var state: MutableState<State> = mutableStateOf(State())
        private set
    private val history = mutableStateOf<List<List<ColorPalette>>>(emptyList())
    private val index = mutableIntStateOf(0)
    var canUndo: MutableState<Boolean> = mutableStateOf(false)
        private set
    var canRedo: MutableState<Boolean> = mutableStateOf(false)
        private set


    init {
        generateRandomPalette(5)
    }

    fun saveInDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertPalette(ColorPaletteEntity(palettes = state.value.colorList))
        }
    }
    fun generateRandomPalette(count: Int) {
        val generateList = mutableListOf<ColorPalette>()
        val random = Random.Default
        var color: Long

        if (state.value.transparency)
            for (i in 0 until count) {
                val locked = state.value.colorList.getOrNull(i)?.locked ?: false
                if (!locked) {
                    color = random.nextLong(0xFFFFFFFF)
                    generateList.add(ColorPalette(hexCode = color))
                } else {
                    generateList.add(state.value.colorList[i])
                }
            }
        else
            for (i in 0 until count) {
                val locked = state.value.colorList.getOrNull(i)?.locked ?: false
                if (!locked) {
                    color = random.nextLong(0xFF000000, 0xFFFFFFFF)
                    generateList.add(ColorPalette(hexCode = color))
                } else {
                    generateList.add(state.value.colorList[i])
                }
            }
        state.value = state.value.copy(colorList = generateList)
        saveInHistory(generateList)
    }

    private fun saveInHistory(colorsList: List<ColorPalette>) {
        val historyList = history.value.toMutableList()
        if(history.value.size >= 5)
        {
            historyList.removeAt(0)
        }
        historyList.add(colorsList)
        history.value = historyList
        index.intValue = history.value.size - 1
        updateCanUndoRedo()
    }

    fun toggleTransparency() {
        state.value = state.value.copy(
            transparency = !state.value.transparency
        )
    }

    fun toggleLockColor(index: Int) {
        state.value.colorList[index].locked = !state.value.colorList[index].locked
    }

    fun redo() {
        if (index.intValue < history.value.size - 1) {
            index.intValue = index.intValue + 1
            state.value = state.value.copy(
                colorList = history.value[index.intValue]
            )
        }
        updateCanUndoRedo()
    }

    fun undo() {
        if (index.intValue > 0) {
            index.intValue = index.intValue - 1
            state.value = state.value.copy(
                colorList = history.value[index.intValue]
            )
        }
        updateCanUndoRedo()
    }

    private fun updateCanUndoRedo() {
        canUndo.value = index.intValue > 0
        canRedo.value = index.intValue < history.value.size - 1
    }

    fun editColor(index: Int, newColor: Color) {
        val red = (newColor.red * 255).toLong()
        val green = (newColor.green * 255).toLong()
        val blue = (newColor.blue * 255).toLong()
        val alpha = (newColor.alpha * 255).toLong()

        val color: Long = (alpha shl 24) or (red shl 16) or (green shl 8) or blue

        state.value.colorList[index].hexCode = color
    }
}

data class State(
    val colorList: List<ColorPalette> = emptyList(),
    val transparency: Boolean = false
)