package com.andrescarvajald.colorpalettegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.andrescarvajald.colorpalettegenerator.domain.database.ColorPaletteDatabase
import com.andrescarvajald.colorpalettegenerator.ui.presentation.MainScreen
import com.andrescarvajald.colorpalettegenerator.ui.theme.ColorPaletteGeneratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeState = remember { mutableStateOf(false) }
            val db = Room.databaseBuilder(
                    application,
                    ColorPaletteDatabase::class.java,
                    "color_palette_generator_db"
                ).fallbackToDestructiveMigration().build()

            ColorPaletteGeneratorTheme(darkTheme = themeState.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(themeState, db)
                }
            }
        }
    }
}
