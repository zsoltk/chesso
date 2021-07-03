package com.github.zsoltk.rf1.ui.composable

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.converter.PgnConverter
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.ui.composable.dialogs.GameDialog
import com.github.zsoltk.rf1.ui.composable.dialogs.ImportDialog
import com.github.zsoltk.rf1.ui.composable.dialogs.PickActiveVisualisationDialog
import com.github.zsoltk.rf1.ui.composable.dialogs.PromotionDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun GameDialogs(
    gamePlayState: MutableState<GamePlayState>,
    gameController: GameController,
    showVizDialog: MutableState<Boolean>,
    showGameDialog: MutableState<Boolean>,
    showImportDialog: MutableState<Boolean>,
) {
    ManagedPromotionDialog(
        showPromotionDialog = gamePlayState.value.uiState.showPromotionDialog,
        gameController = gameController
    )
    ManagedVizDialog(
        showVizDialog = showVizDialog,
        gameController = gameController
    )

    ManagedGameDialog(
        showGameDialog = showGameDialog,
        showImportDialog = showImportDialog,
        gameState = gamePlayState.value.gameState,
        gameController = gameController,
    )

    ManagedImportDialog(
        showImportDialog = showImportDialog,
        gamePlayState = gamePlayState
    )
}

@Composable
fun ManagedPromotionDialog(
    showPromotionDialog: Boolean,
    gameController: GameController,
) {
    if (showPromotionDialog) {
        PromotionDialog(gameController.toMove) {
            gameController.onPromotionPieceSelected(it)
        }
    }
}

@Composable
fun ManagedVizDialog(
    showVizDialog: MutableState<Boolean>,
    gameController: GameController,
) {
    if (showVizDialog.value) {
        PickActiveVisualisationDialog(
            onDismiss = {
                showVizDialog.value = false
            },
            onItemSelected = {
                showVizDialog.value = false
                gameController.setVisualisation(it)
            }
        )
    }
}

@Composable
fun ManagedGameDialog(
    showGameDialog: MutableState<Boolean>,
    showImportDialog: MutableState<Boolean>,
    gameState: GameState,
    gameController: GameController,
) {
    if (showGameDialog.value) {
        val context = LocalContext.current

        GameDialog(
            onDismiss = {
                showGameDialog.value = false
            },
            onNewGame = {
                showGameDialog.value = false
                gameController.reset()
            },
            onImportGame = {
                showGameDialog.value = false
                showImportDialog.value = true
            },
            onExportGame = {
                showGameDialog.value = false
                val pgn = PgnConverter.export(gameState)
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, pgn)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                ContextCompat.startActivity(context, shareIntent, Bundle())
            }
        )
    }
}

@Composable
fun ManagedImportDialog(
    showImportDialog: MutableState<Boolean>,
    gamePlayState: MutableState<GamePlayState>,
) {
    val (pgnToImport, setPgnToImport) = remember { mutableStateOf("") }

    if (showImportDialog.value) {
        ImportDialog(
            onDismiss = {
                showImportDialog.value = false
            },
            onImport = { pgn ->
                showImportDialog.value = false
                setPgnToImport(pgn)
            }
        )
    }

    ManagedImport(
        pgnToImport = pgnToImport,
        gamePlayState = gamePlayState,
        setPgnToImport = setPgnToImport
    )
}

@Composable
private fun ManagedImport(
    pgnToImport: String,
    gamePlayState: MutableState<GamePlayState>,
    setPgnToImport: (String) -> Unit,
) {
    if (pgnToImport.isNotBlank()) {
        LoadingSpinner()
    }

    LaunchedEffect(pgnToImport) {
        if (pgnToImport.isNotBlank()) {
            withContext(Dispatchers.IO) {
                val importedState = PgnConverter.import(pgnToImport)
                val importedGame = GamePlayState(importedState)
                gamePlayState.value = importedGame

                setPgnToImport("")
            }
        }
    }
}

