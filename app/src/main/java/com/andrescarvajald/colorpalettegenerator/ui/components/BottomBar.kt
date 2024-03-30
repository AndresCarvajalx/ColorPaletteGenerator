package com.andrescarvajald.colorpalettegenerator.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Redo
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andrescarvajald.colorpalettegenerator.R
import com.andrescarvajald.colorpalettegenerator.viewModel.MainScreenViewModel
import kotlinx.coroutines.launch


@Composable
fun BottomBar(viewModel: MainScreenViewModel, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    var value by remember { mutableStateOf("5") }
    val showSheetOptions = remember { mutableStateOf(false) }
    if(showSheetOptions.value) {
        BottomSheetOptions(showSheetOptions = showSheetOptions, viewModel = viewModel)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { viewModel.generateRandomPalette(value.toInt()) },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            contentPadding = PaddingValues(horizontal = 12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Generate")
        }

        Row (verticalAlignment = Alignment.CenterVertically){
            IconButton(modifier = Modifier.width(30.dp), onClick = { viewModel.undo() }, enabled = viewModel.canUndo.value ) {
                Icon(imageVector = Icons.Rounded.Undo, contentDescription = "Undo")
            }
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(modifier = Modifier.width(30.dp), onClick = { viewModel.redo() }, enabled = viewModel.canRedo.value) {
                Icon(imageVector = Icons.Rounded.Redo, contentDescription = "Redo")
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    modifier = Modifier.height(20.dp),
                    onClick = {
                        if (value.toInt() < 10) {
                            value = (value.toInt() + 1).toString()
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowDropUp, contentDescription = null)
                }
                IconButton(
                    modifier = Modifier.height(20.dp),
                    onClick = {
                        if (value.toInt() > 1) {
                            value = (value.toInt() - 1).toString()
                        }
                    }
                ) {

                    Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
                }
            }
            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                        MaterialTheme.shapes.small
                    )
                    .padding(10.dp),
                style = MaterialTheme.typography.labelSmall,
                text = value,
                textAlign = TextAlign.Center
                )
            IconButton(onClick = {
                showSheetOptions.value = !showSheetOptions.value
            }) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.options_icon), contentDescription = null)
            }
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    }
}