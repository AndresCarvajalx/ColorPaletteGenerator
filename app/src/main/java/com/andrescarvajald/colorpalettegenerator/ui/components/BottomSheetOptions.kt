package com.andrescarvajald.colorpalettegenerator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.InvertColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andrescarvajald.colorpalettegenerator.viewModel.MainScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetOptions(
    showSheetOptions: MutableState<Boolean>,
    viewModel: MainScreenViewModel
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    ModalBottomSheet(
        onDismissRequest = { showSheetOptions.value = false },
        sheetState = sheetState
    ) {
        // TODO REFACTOR, USAR UNA LISTA PARA REDUCIR CODIGO O UNA FUNCION cambiar el tip del top bar
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        viewModel.toggleTransparency()
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Rounded.InvertColors,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = "Transparency")
                    }
                    Switch(
                        checked = viewModel.state.value.transparency,
                        enabled = true,
                        onCheckedChange = { viewModel.toggleTransparency() },
                    )

                }
            }
            Divider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        viewModel.saveInDatabase()
                        showSheetOptions.value = false
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
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Save Palette")
                }
            }
            Divider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSheetOptions.value = false }
                    .also {
                        Modifier.padding(15.dp)
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(modifier = Modifier.padding(15.dp),text = "Cancel", style = MaterialTheme.typography.labelLarge)
            }
        }

    }
}
