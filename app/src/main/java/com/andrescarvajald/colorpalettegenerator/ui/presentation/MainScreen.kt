package com.andrescarvajald.colorpalettegenerator.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andrescarvajald.colorpalettegenerator.domain.database.ColorPaletteDatabase
import com.andrescarvajald.colorpalettegenerator.ui.components.BottomBar
import com.andrescarvajald.colorpalettegenerator.ui.components.ColorPaletteCard
import com.andrescarvajald.colorpalettegenerator.ui.components.TopBar
import com.andrescarvajald.colorpalettegenerator.viewModel.MainScreenViewModel
import kotlinx.coroutines.launch

/*
* TODO  [X]Funcionalidad del boton de bloqueo
* TODO  [X]Mostrar colores icono de colores bloqueados
* TODO  [X]cambiar los iconos y pasarlos como parametros al drawerItem
* TODO  [X]Al darle click al hex del color poder editarlos
* TODO  []Drag and drop colors
* TODO  []Undo y redo botones
* TODO  []Funcionalidad del boton de historia
* TODO  []Ejemplos
* */
/*
data class DrawerSheet(
    val title: String = "",
    val icon: ImageVector,
    val selected: Boolean = false
){
    val drawerSheet = listOf<DrawerSheet>(
        DrawerSheet(
            title = "Color Palette",
            icon = Icons.Rounded.Palette,
            selected = true
        ),
        DrawerSheet(
            title = "History",
            icon = Icons.Rounded.History,
            selected = false
        ),
        DrawerSheet(
            title = "Transparency",
            icon = Icons.Rounded.InvertColors,
            selected = false
        ),
    )
}
 */

@Composable
fun MainScreen(themeState: MutableState<Boolean>, db: ColorPaletteDatabase) {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: MainScreenViewModel = viewModel<MainScreenViewModel> {
        MainScreenViewModel(db.colorPaletteDao)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selected by rememberSaveable { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        gesturesEnabled = true,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Color Palette Options",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Divider()
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    NavigationDrawerItem(
                        label = { Text(text = "Color Palette") },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Palette,
                                contentDescription = null
                            )
                        },
                        selected = selected == 0,
                        onClick = {
                            selected = 0
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Saves") },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        selected = selected == 1,
                        onClick = {
                            selected = 1
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Examples") },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Apps,
                                contentDescription = null
                            )
                        },
                        selected = selected == 2,
                        onClick = {
                            selected = 2
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        },
    ) {
        if (selected == 0) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { TopBar(themeState) },
                bottomBar = { BottomBar(viewModel, drawerState) },
                snackbarHost = {
                    SnackbarHost(snackbarHostState) { data ->
                        Snackbar(
                            dismissActionContentColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            snackbarData = data
                        )
                    }
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(viewModel.state.value.colorList, key = { c -> c.hexCode }) { color ->
                        ColorPaletteCard(color, snackbarHostState, viewModel, {/*TODO*/}) {
                            viewModel.toggleLockColor(
                                viewModel.state.value.colorList.indexOf(
                                    color
                                )
                            )
                        }
                    }
                }
            }
        }
        if (selected == 1) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { TopBar(themeState) },
                snackbarHost = {
                    SnackbarHost(snackbarHostState) { data ->
                        Snackbar(
                            dismissActionContentColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            snackbarData = data
                        )
                    }
                }
            ) { paddingValues ->
                Surface(Modifier.padding(paddingValues).padding(horizontal = 8.dp)){
                    SavesPalettesScreen(db, snackbarHostState)
                }
            }
        }

        if (selected == 2) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Coming soon", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
