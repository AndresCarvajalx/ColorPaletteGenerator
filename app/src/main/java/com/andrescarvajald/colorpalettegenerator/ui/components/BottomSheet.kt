package com.andrescarvajald.colorpalettegenerator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.andrescarvajald.colorpalettegenerator.util.CopyToClipBoard
import com.andrescarvajald.colorpalettegenerator.util.ToHexString
import com.andrescarvajald.colorpalettegenerator.viewModel.MainScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    state: MutableState<Boolean>,
    color: Long,
    viewModel: MainScreenViewModel,
    showSnackBar: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val index = viewModel.state.value.colorList.indexOfFirst { c -> c.hexCode == color }
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { true })
    val red = remember { mutableFloatStateOf(Color(color).red) }
    val green = remember { mutableFloatStateOf(Color(color).green) }
    val blue = remember { mutableFloatStateOf(Color(color).blue) }
    val alpha = remember { mutableFloatStateOf(Color(color).alpha) }
    val colorPreview = Color(
        red.floatValue,
        green.floatValue,
        blue.floatValue,
        alpha.floatValue
    )
    val redColor = Color(
        red.floatValue,
        0f,
        0f,
    )
    val greenColor = Color(
        0f,
        green.floatValue,
        0f
    )
    val blueColor = Color(
        0f,
        0f,
        blue.floatValue
    )
    val (inputColor, setInputColor) = remember {
        mutableStateOf("")
    }
    ModalBottomSheet(
        onDismissRequest = {
            viewModel.editColor(index, colorPreview)
            state.value = !state.value
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .padding(bottom = 10.dp)
        ) {
            var btnColorState: Color? by remember {
                mutableStateOf(Color(0xFF167722))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Insert hexcode: #", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    minLines = 1,
                    singleLine = true,
                    placeholder = { Text(text = ToHexString(color)) },
                    value = inputColor,
                    onValueChange = {
                        setInputColor(it)
                        if (inputColor.isNotEmpty() && inputColor.isNotBlank()) {
                            btnColorState = try {
                                Color(it.toLong(radix = 16))
                            } catch (e: Exception) {
                                null
                            }
                        }
                    }
                )

                IconButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (btnColorState != null) btnColorState!! else Color(0xFF167722)),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (btnColorState != null) btnColorState!! else Color(
                            0xFF167722
                        ),
                        contentColor = Color.White
                    ),
                    onClick = {
                        if (btnColorState != null && inputColor.isNotBlank() && inputColor.isNotBlank()) {
                            red.floatValue = btnColorState!!.red
                            green.floatValue = btnColorState!!.green
                            blue.floatValue = btnColorState!!.blue
                            alpha.floatValue = btnColorState!!.alpha
                        }
                    }) {
                    Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorPreview)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Red", style = MaterialTheme.typography.labelLarge)
            Slider(
                value = red.floatValue, onValueChange = { r -> red.floatValue = r },
                colors = SliderDefaults.colors(
                    activeTrackColor = redColor,
                    activeTickColor = redColor
                )
            )
            Text(text = "Green", style = MaterialTheme.typography.labelLarge)
            Slider(
                value = green.floatValue, onValueChange = { g -> green.floatValue = g },
                colors = SliderDefaults.colors(
                    activeTrackColor = greenColor,
                    activeTickColor = greenColor
                )
            )
            Text(text = "Blue", style = MaterialTheme.typography.labelLarge)
            Slider(
                value = blue.floatValue,
                onValueChange = { b -> blue.floatValue = b },
                colors = SliderDefaults.colors(
                    activeTrackColor = blueColor,
                    activeTickColor = blueColor
                )
            )
            Text(text = "Alpha", style = MaterialTheme.typography.labelLarge)
            Slider(
                value = alpha.floatValue,
                onValueChange = { p -> alpha.floatValue = p }
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        val c = Color(color)
                        red.floatValue = c.red
                        green.floatValue = c.green
                        blue.floatValue = c.blue
                        alpha.floatValue = c.alpha
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = "Reset color"
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(
                        onClick = {
                            val hexCode = ToHexString(color)
                            CopyToClipBoard(context, hexCode, "#")
                            showSnackBar(hexCode)
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                state.value = false
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = "Copy color"
                        )
                    }
                }
            }
        }
    }
}
