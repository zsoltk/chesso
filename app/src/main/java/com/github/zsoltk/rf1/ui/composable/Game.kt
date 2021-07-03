package com.github.zsoltk.rf1.ui.composable

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.dataviz.ActiveDatasetVisualisation
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.game.converter.PgnConverter
import com.github.zsoltk.rf1.model.game.preset.Preset
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.ui.Rf1Theme
import com.github.zsoltk.rf1.ui.alice_blue
import com.github.zsoltk.rf1.ui.composable.dialogs.GameDialog
import com.github.zsoltk.rf1.ui.composable.dialogs.ImportDialog
import com.github.zsoltk.rf1.ui.composable.dialogs.PickActiveVisualisationDialog
import com.github.zsoltk.rf1.ui.composable.dialogs.PromotionDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

@Composable
fun Game(state: GamePlayState = GamePlayState(), preset: Preset? = null) {
    var isFlipped by rememberSaveable { mutableStateOf(false) }
    var gamePlayState by rememberSaveable { mutableStateOf(state) }
    var showVizDialog by remember { mutableStateOf(false) }
    var showGameDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    val gameController = remember {
        GameController(
            getGamePlayState = { gamePlayState },
            setGamePlayState = { gamePlayState = it },
            preset = preset
        )
    }

    CompositionLocalProvider(ActiveDatasetVisualisation provides gamePlayState.visualisation) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Status(gamePlayState.gameState)
            Moves(gamePlayState.gameState, onClickMove = { gameController.goToMove(it) })
            CapturedPieces(gamePlayState.gameState)
            Board(
                gamePlayState = gamePlayState,
                gameController = gameController,
                isFlipped = isFlipped
            )
            GameControls(
                gamePlayState = gamePlayState,
                onStepBack = { gameController.stepBackward() },
                onStepForward = { gameController.stepForward() },
                onVizClicked = { showVizDialog = true },
                onFlipBoard = { isFlipped = !isFlipped },
                onGameClicked = { showGameDialog = true }
            )
        }

        if (gamePlayState.uiState.showPromotionDialog) {
            PromotionDialog(gameController.toMove) {
                gameController.onPromotionPieceSelected(it)
            }
        }
        if (showVizDialog) {
            PickActiveVisualisationDialog(
                onDismiss = {
                    showVizDialog = false
                },
                onItemSelected = {
                    showVizDialog = false
                    gameController.setVisualisation(it)
                }
            )
        }
        if (showGameDialog) {
            val context = LocalContext.current

            GameDialog(
                onDismiss = {
                    showGameDialog = false
                },
                onNewGame = {
                    showGameDialog = false
                    gameController.reset()
                },
                onImportGame = {
                    showGameDialog = false
                    showImportDialog = true
                },
                onExportGame = {
                    showGameDialog = false
                    val pgn = PgnConverter.export(gamePlayState.gameState)
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, pgn)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(context, shareIntent, Bundle())
                }
            )
        }

        var pgnToImport by remember { mutableStateOf("") }
        if (showImportDialog) {
            ImportDialog(
                onDismiss = {
                    showImportDialog = false
                },
                onImport = { pgn ->
                    showImportDialog = false
                    pgnToImport = pgn
                }
            )
        }
        LaunchedEffect(pgnToImport) {
            if (pgnToImport.isNotBlank()) {
                withContext(Dispatchers.IO) {
                    async {
                        gamePlayState = GamePlayState(PgnConverter.import(pgnToImport))
                    }.await()

                    pgnToImport = ""
                }
            }
        }
        if (pgnToImport.isNotBlank()) {
            MaterialTheme {
                Dialog(
                    onDismissRequest = {},
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                ) {
                    CircularProgressIndicator(
                        color = alice_blue
                    )
                }
            }
        }
    }
}

@Composable
private fun Status(gameState: GameState) {
    val text = when (gameState.resolution) {
        Resolution.IN_PROGRESS -> "${gameState.toMove} to move"
        else -> gameState.resolution.toString().replace("_", " ")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
private fun GameControls(
    gamePlayState: GamePlayState,
    onStepBack: () -> Unit,
    onStepForward: () -> Unit,
    onVizClicked: () -> Unit,
    onFlipBoard: () -> Unit,
    onGameClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Button(
            onClick = onStepBack,
            enabled = gamePlayState.gameState.hasPrevIndex
        ) {
            Text("<")
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onStepForward,
            enabled = gamePlayState.gameState.hasNextIndex
        ) {
            Text(">")
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onVizClicked,
        ) {
            Text("Viz")
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onFlipBoard,
        ) {
            Text("Flip")
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onGameClicked,
        ) {
            Text("Game")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    Rf1Theme {
        var gamePlayState = GamePlayState()
        GameController({ gamePlayState }, { gamePlayState = it }).apply {
            applyMove(e2, e4)
            applyMove(e7, e5)
            applyMove(b1, c3)
            applyMove(b8, c6)
            applyMove(f1, b5)
            applyMove(d7, d5)
            applyMove(e4, d5)
            applyMove(d8, d5)
            applyMove(c3, d5)
            onClick(g8)
        }
        Game(
            state = gamePlayState,
        )
    }
}
