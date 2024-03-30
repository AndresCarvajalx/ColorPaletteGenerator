package com.andrescarvajald.colorpalettegenerator.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TipsAndUpdates
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HelpDialog(showQuestionDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { showQuestionDialog.value = !showQuestionDialog.value },
        confirmButton = {
            TextButton(onClick = { showQuestionDialog.value = !showQuestionDialog.value }) {
                Text(text = "Confirm")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Tip")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Rounded.TipsAndUpdates,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },
        text = {
            Text(
                text = "Some colors might have transparency so is recommended toggle the app " +
                        "theme using the button on the right side of the app title." +
                        "The transparency is disable by default, to enable it open the slide menu " +
                        "and touch transparency in the menu options"
            )
        }
    )
}