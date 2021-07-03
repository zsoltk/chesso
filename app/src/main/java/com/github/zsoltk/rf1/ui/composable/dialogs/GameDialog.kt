package com.github.zsoltk.rf1.ui.composable.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GameDialog(
    onDismiss: () -> Unit,
    onNewGame: () -> Unit,
    onImportGame: () -> Unit,
    onExportGame: () -> Unit,
) {
    ClickableListItemsDialog(
        onDismiss = onDismiss,
        items = listOf(
            "New" to onNewGame,
            "Import" to onImportGame,
            "Export" to onExportGame,
        )
    )
}

@Preview
@Composable
private fun GameDialogContent() {
    GameDialog(
        onDismiss = {},
        onNewGame = {},
        onImportGame = {},
        onExportGame = {},
    )
}
