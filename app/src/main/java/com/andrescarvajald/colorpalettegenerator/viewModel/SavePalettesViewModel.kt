package com.andrescarvajald.colorpalettegenerator.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrescarvajald.colorpalettegenerator.domain.dao.ColorPaletteDao
import com.andrescarvajald.colorpalettegenerator.model.ColorPaletteEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavePalettesViewModel(
    private val paletteDao: ColorPaletteDao
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state

    init {
        viewModelScope.launch {
            getPalettes()
        }
    }

    private suspend fun getPalettes() {
        paletteDao.getPalette()
            .collect { palette ->
                _state.value = _state.value.copy(palettes = palette)
            }
    }
    fun removePalette(palette: ColorPaletteEntity) {
        viewModelScope.launch {
            paletteDao.removePalette(palette)
        }
    }

    data class UIState(val palettes: List<ColorPaletteEntity> = emptyList())
}