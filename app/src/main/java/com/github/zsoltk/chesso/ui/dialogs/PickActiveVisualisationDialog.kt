package com.github.zsoltk.chesso.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.zsoltk.chesso.model.dataviz.ActiveDatasetVisualisation
import com.github.zsoltk.chesso.model.dataviz.DatasetVisualisation
import com.github.zsoltk.chesso.model.dataviz.datasetVisualisations

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            ),
        horizontalAlignment = Alignment.Start
    ) {
        datasetVisualisations.forEach { item ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable(onClick = { onItemSelected(item) }),
            ) {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (ActiveDatasetVisualisation.current == item) {
                        Text(text = "✓")
                    }
                }

                Text(
                    text = stringResource(item.name),
                    modifier = Modifier
                        .padding(4.dp),
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}
