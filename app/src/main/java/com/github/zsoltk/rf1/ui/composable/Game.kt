package com.github.zsoltk.rf1.ui.composable

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.zsoltk.rf1.model.board.Position.b1
import com.github.zsoltk.rf1.model.board.Position.b5
import com.github.zsoltk.rf1.model.board.Position.b8
import com.github.zsoltk.rf1.model.board.Position.c3
import com.github.zsoltk.rf1.model.board.Position.c6
import com.github.zsoltk.rf1.model.board.Position.d5
import com.github.zsoltk.rf1.model.board.Position.d7
import com.github.zsoltk.rf1.model.board.Position.d8
import com.github.zsoltk.rf1.model.board.Position.e2
import com.github.zsoltk.rf1.model.board.Position.e4
import com.github.zsoltk.rf1.model.board.Position.e5
import com.github.zsoltk.rf1.model.board.Position.e7
import com.github.zsoltk.rf1.model.board.Position.f1
import com.github.zsoltk.rf1.model.board.Position.g8
import com.github.zsoltk.rf1.model.dataviz.ActiveDatasetVisualisation
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.preset.Preset
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.ui.Rf1Theme

@Composable
fun Game(
    state: GamePlayState = GamePlayState(),
    importGameText: String? = null,
    preset : Preset ? = null,
) {
    var isFlipped by rememberSaveable { mutableStateOf(false) }
    val gamePlayState = rememberSaveable { mutableStateOf(state) }
    val showVizDialog = remember { mutableStateOf(false) }
    val showGameDialog = remember { mutableStateOf(false) }
    val showImportDialog = remember { mutableStateOf(false) }
    val pgnToImport = remember { mutableStateOf(importGameText) }

    val gameController = remember {
        GameController(
            getGamePlayState = { gamePlayState.value },
            setGamePlayState = { gamePlayState.value = it },
            preset = preset
        )
    }

    CompositionLocalProvider(ActiveDatasetVisualisation provides gamePlayState.value.visualisation) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Status(gamePlayState.value.gameState)
            Moves(gamePlayState.value.gameState, onClickMove = { gameController.goToMove(it) })
            CapturedPieces(gamePlayState.value.gameState)
            Board(
                gamePlayState = gamePlayState.value,
                gameController = gameController,
                isFlipped = isFlipped
            )
            GameControls(
                gamePlayState = gamePlayState.value,
                onStepBack = { gameController.stepBackward() },
                onStepForward = { gameController.stepForward() },
                onVizClicked = { showVizDialog.value = true },
                onFlipBoard = { isFlipped = !isFlipped },
                onGameClicked = { showGameDialog.value = true }
            )
        }

        GameDialogs(
            gamePlayState = gamePlayState,
            gameController = gameController,
            showVizDialog = showVizDialog,
            showGameDialog = showGameDialog,
            showImportDialog = showImportDialog,
            pgnToImport = pgnToImport,
        )

        ManagedImport(
            pgnToImport = pgnToImport,
            gamePlayState = gamePlayState,
        )
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
