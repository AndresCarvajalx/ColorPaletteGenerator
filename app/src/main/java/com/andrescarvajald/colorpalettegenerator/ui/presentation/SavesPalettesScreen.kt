package com.andrescarvajald.colorpalettegenerator.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andrescarvajald.colorpalettegenerator.domain.database.ColorPaletteDatabase
import com.andrescarvajald.colorpalettegenerator.model.ColorPaletteEntity
import com.andrescarvajald.colorpalettegenerator.viewModel.MainScreenViewModel
import com.andrescarvajald.colorpalettegenerator.viewModel.SavePalettesViewModel
import kotlinx.coroutines.launch

@Composable
fun SavesPalettesScreen(
    db: ColorPaletteDatabase,
    snackbarHostState: SnackbarHostState,
    mainScreenViewModel: MainScreenViewModel
) {
    val savePalettesViewModel: SavePalettesViewModel =
        viewModel<SavePalettesViewModel> { SavePalettesViewModel(db.colorPaletteDao) }
    val state = savePalettesViewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    if (state.value.palettes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Nothing yet...",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn {
            items(state.value.palettes) { palettes ->
                PaletteCard(
                    palettes,
                    onRemove = {
                        savePalettesViewModel.removePalette(palettes)
                        scope.launch {
                            snackbarHostState.showSnackbar("The palette was removed")
                        }
                    },
                    onRestore = {
                        scope.launch {
                            mainScreenViewModel.generateRandomPalette(colorList = palettes.palettes)
                            snackbarHostState.showSnackbar("Palette restored")
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun PaletteCard(
    palettes: ColorPaletteEntity,
    onRemove: () -> Unit,
    onRestore: () -> Unit
) {
    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    if (showBottomSheet.value) {
        BottomSheetOptions(showState = showBottomSheet, onRemove = {
            onRemove()
            showBottomSheet.value = false
        }, onRestore = {
            onRestore()
            showBottomSheet.value = false
        })
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                showBottomSheet.value = !showBottomSheet.value
            }) {
        palettes.palettes.forEach { color ->
            val background = Color(color.hexCode)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(background)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetOptions(
    showState: MutableState<Boolean>,
    onRemove: () -> Unit,
    onRestore: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    ModalBottomSheet(onDismissRequest = { showState.value = false }, sheetState = sheetState) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        onRestore()
                    }
                    .also {
                        Modifier.padding(10.dp)
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Restore,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Restore Palette")
                }
            }
            Divider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        onRemove()
                    }
                    .also {
                        Modifier.padding(10.dp)
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.RemoveCircle,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Remove Palette")
                }
            }
            Divider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showState.value = false }
                    .also {
                        Modifier.padding(15.dp)
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.padding(15.dp),
                    text = "Cancel",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}