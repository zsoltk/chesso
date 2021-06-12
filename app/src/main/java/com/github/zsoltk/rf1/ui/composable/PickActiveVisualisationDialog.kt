package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.zsoltk.rf1.model.dataviz.ActiveDatasetVisualisation
import com.github.zsoltk.rf1.model.dataviz.DatasetVisualisation
import com.github.zsoltk.rf1.model.dataviz.datasetVisualisations

@Composable
fun PickActiveVisualisationDialog(
    onDismiss: () -> Unit,
    onItemSelected: (DatasetVisualisation) -> Unit
) {
    MaterialTheme {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            PickActiveVisualisationDialogContent {
                onItemSelected(it)
            }
        }
    }
}

@Preview
@Composable
private fun PickActiveVisualisationDialogContent(
    onItemSelected: (DatasetVisualisation) -> Unit = {}
) {
    Column(
        modifier = Modifier.background(
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.medium
        ),
        horizontalAlignment = Alignment.Start
    ) {
        datasetVisualisations.forEach { item ->
            Row {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (ActiveDatasetVisualisation.current == item) {
                        Text(text = "✓")
                    }
                }

                Text(
                    text = item.name,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable(onClick = { onItemSelected(item) }),
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}
