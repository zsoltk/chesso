package com.github.zsoltk.chesso.ui.composable

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
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.board.Position.*
import com.github.zsoltk.chesso.model.dataviz.ActiveDatasetVisualisation
import com.github.zsoltk.chesso.model.game.controller.GameController
import com.github.zsoltk.chesso.model.game.preset.Preset
import com.github.zsoltk.chesso.model.game.state.GamePlayState
import com.github.zsoltk.chesso.model.game.state.GameState
import com.github.zsoltk.chesso.model.piece.Set
import com.github.zsoltk.chesso.ui.ChessoTheme

@Composable
fun Game(
    state: GamePlayState = GamePlayState(),
    importGameText: String? = null,
    preset: Preset? = null,
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
            CapturedPieces(
                gameState = gamePlayState.value.gameState,
                capturedBy = if (isFlipped) Set.WHITE else Set.BLACK,
                arrangement = Arrangement.Start,
                scoreAlignment = Alignment.End,
            )
            Board(
                gamePlayState = gamePlayState.value,
                gameController = gameController,
                isFlipped = isFlipped
            )
            CapturedPieces(
                gameState = gamePlayState.value.gameState,
                capturedBy = if (isFlipped) Set.BLACK else Set.WHITE,
                arrangement = Arrangement.End,
                scoreAlignment = Alignment.Start
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(gameState.resolutionText()),
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = onStepBack,
            enabled = gamePlayState.gameState.hasPrevIndex
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_keyboard_arrow_left),
                tint = LocalTextStyle.current.color,
                contentDescription = stringResource(R.string.action_previous_move)
            )
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onStepForward,
            enabled = gamePlayState.gameState.hasNextIndex
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_keyboard_arrow_right),
                tint = LocalTextStyle.current.color,
                contentDescription = stringResource(R.string.action_next_move)
            )
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onVizClicked,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_layers),
                tint = LocalTextStyle.current.color,
                contentDescription = stringResource(R.string.action_pick_active_visualisation)
            )
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onFlipBoard,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_loop),
                tint = LocalTextStyle.current.color,
                contentDescription = stringResource(R.string.action_flip)
            )
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onGameClicked,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                tint = LocalTextStyle.current.color,
                contentDescription = stringResource(R.string.action_game_menu)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    ChessoTheme {
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
