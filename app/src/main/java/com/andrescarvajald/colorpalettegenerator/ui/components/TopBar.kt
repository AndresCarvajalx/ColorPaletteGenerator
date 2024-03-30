package com.andrescarvajald.colorpalettegenerator.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(themeState: MutableState<Boolean>) {
    var tintColor by remember { mutableStateOf(if (themeState.value) Color.White else Color.Black) }
    val showQuestionDialog = remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue = tintColor,
        label = "",
        animationSpec = tween(500)
    )
    if (showQuestionDialog.value) {
        HelpDialog(showQuestionDialog = showQuestionDialog)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = {
                showQuestionDialog.value = true
            }) {
                Icon(
                    imageVector = Icons.Rounded.QuestionMark,
                    contentDescription = null,
                    tint = animatedColor.copy(alpha = 0.5f)
                )
            }
        }

        Text(
            text = "Color Palette",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(onClick = {
                themeState.value = !themeState.value
                tintColor = if (themeState.value) Color.White else Color.Black
            }) {
                Icon(
                    imageVector = if (themeState.value) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                    contentDescription = null,
                    tint = animatedColor
                )
            }
        }
    }
}