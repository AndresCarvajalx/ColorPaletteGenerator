package com.andrescarvajald.colorpalettegenerator.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.andrescarvajald.colorpalettegenerator.R
import com.andrescarvajald.colorpalettegenerator.model.ColorPalette
import com.andrescarvajald.colorpalettegenerator.util.CopyToClipBoard
import com.andrescarvajald.colorpalettegenerator.util.ToHexString
import com.andrescarvajald.colorpalettegenerator.util.IsDarkColor
import com.andrescarvajald.colorpalettegenerator.viewModel.MainScreenViewModel
import kotlinx.coroutines.launch


@Composable
fun ColorPaletteCard(
    paletteColor: ColorPalette,
    snackbarHostState: SnackbarHostState,
    viewModel: MainScreenViewModel,
    onDrag: () -> Unit,
    onBlockColor: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    var showActions by remember { mutableStateOf(false) }
    var locked by remember(paletteColor.locked) { mutableStateOf(paletteColor.locked) }
    val hexCodeFormatted = ToHexString(paletteColor.hexCode)
    val colorAccent = if (IsDarkColor(paletteColor.hexCode)) Color.White else Color.Black
    val color = Color(paletteColor.hexCode)
    val factor = 0.8F
    val showBottomSheet = remember { mutableStateOf(false) }
    if (showBottomSheet.value) {
        BottomSheet(showBottomSheet, paletteColor.hexCode, viewModel) { hexCode ->
            scope.launch {
                snackbarHostState.showSnackbar("Color copied to clipboard $hexCode")
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color)
            .clickable {
                showActions = !showActions
            }, contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            IconButton(onClick = { onDrag() }) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.drag_grid_2x3_icon), contentDescription = null, tint = color.copy(red = color.red * factor, green = color.green * factor, blue = color.blue * factor))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { showBottomSheet.value = true }) {
                Text(
                    text = hexCodeFormatted,
                    color = colorAccent,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            AnimatedVisibility(showActions) {
                Row {
                    IconButton(
                        onClick = {
                            scope.launch {
                                CopyToClipBoard(ctx, hexCodeFormatted, "#")
                                snackbarHostState.showSnackbar(
                                    "Color copied to clipboard #$hexCodeFormatted",
                                    withDismissAction = true
                                )
                            }
                            showActions = !showActions
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = "copy",
                            tint = colorAccent
                        )
                    }
                    IconButton(onClick = {
                        onBlockColor()
                        showActions = !showActions
                    }) {
                        Icon(
                            imageVector = if (paletteColor.locked) {
                                Icons.Rounded.Lock
                            } else {
                                Icons.Rounded.LockOpen
                            },
                            contentDescription = "lock",
                            tint = colorAccent
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp), contentAlignment = Alignment.CenterEnd
        ) {
            if (locked && !showActions) {
                IconButton(onClick = {
                    locked = false
                    onBlockColor()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "lock",
                        tint = colorAccent
                    )
                }
            }
        }
    }
}

