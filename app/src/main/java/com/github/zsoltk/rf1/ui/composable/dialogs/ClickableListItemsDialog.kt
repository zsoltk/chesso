package com.github.zsoltk.rf1.ui.composable.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ClickableListItemsDialog(
    onDismiss: () -> Unit,
    items: List<Pair<String, () -> Unit>>
) {
    MaterialTheme {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            VerticalClickableList(items)
        }
    }
}

@Composable
private fun VerticalClickableList(items: List<Pair<String, () -> Unit>>) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            items.forEach { (text, onClick) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onClick),
                ) {
                    Box(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = text,
                            color = MaterialTheme.colors.onSurface,
                        )
                    }
                }
            }

        }
    }
}
