package com.andrescarvajald.colorpalettegenerator.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TipsAndUpdates
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class TipPage(
    val description: String,
)

val pages = listOf(
    TipPage(
        description = "Some colors might have transparency, so it is recommended to toggle the app theme using the button on the right side of the app title. " +
                "Transparency is disabled by default. To enable it, open the bottom sheet menu and touch 'Transparency' in the menu options."
    ),

    TipPage(
        description = "Long press the up arrow to move the color to the first position. Long press the down arrow to move the color to the last position."
    ),

    TipPage(
        description = "Tap the color to show actions."
    ),

    TipPage(
        description = "Tap the color hex code to show a sheet to modify it."
    ),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HelpDialog(showQuestionDialog: MutableState<Boolean>) {
    val state = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = { showQuestionDialog.value = !showQuestionDialog.value },
        confirmButton = {
            TextButton(onClick = { showQuestionDialog.value = !showQuestionDialog.value }) {
                Text(text = "Confirm")
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = "Tip")
                Icon(
                    imageVector = Icons.Rounded.TipsAndUpdates,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                repeat(pages.size) { i ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1f)
                            .height(8.dp)
                            .background(if (state.currentPage == i) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.secondary)
                            .clickable {
                                scope.launch {
                                    state.animateScrollToPage(i)
                                }
                            }
                    )
                }
            }
        },
        text = {
            HorizontalPager(state = state) { index ->
                Text(
                    text = pages[index].description
                )
            }
        }
    )
}