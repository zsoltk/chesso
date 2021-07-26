package com.github.zsoltk.chesso.ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.chesso.R

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
            stringResource(R.string.game_new) to onNewGame,
            stringResource(R.string.game_import) to onImportGame,
            stringResource(R.string.game_export) to onExportGame,
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
