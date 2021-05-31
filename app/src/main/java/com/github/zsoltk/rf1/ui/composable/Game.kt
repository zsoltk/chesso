package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.game.Game
import com.github.zsoltk.rf1.model.game.GameController
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.game.state.UiState
import com.github.zsoltk.rf1.ui.Rf1Theme

@Composable
fun Game(game: Game = Game(), uiState: UiState = UiState()) {
    var showPromotionDialog by remember { mutableStateOf(false) }
    val onPromotion = { showPromotionDialog = true }
    val gameController = remember { GameController(game, uiState, onPromotion) }

    Column {
        ToMove(game)
        Moves(game)
        CapturedPieces(game)
        AnimatedBoard(
            gameController = gameController
        )
        Spacer(modifier = Modifier.height(48.dp))
        TimeTravelButtons(gameController)
    }

    if (showPromotionDialog) {
        PromotionDialog(gameController.toMove) {
            gameController.onPromotionPieceSelected(it)
            showPromotionDialog = false
        }
    }
}

@Composable
private fun ToMove(game: Game) {
    val text = when (game.resolution) {
        Resolution.IN_PROGRESS -> "${game.toMove} to move"
        else -> game.resolution.toString().replace("_", " ")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(MaterialTheme.colors.primaryVariant),
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
private fun TimeTravelButtons(gameController: GameController) {
    Row {
        Button(
            onClick = { gameController.stepBackward() },
            enabled = gameController.canStepBack()
        ) {
            Text("<")
        }
        Button(
            onClick = { gameController.stepForward() },
            enabled = gameController.canStepForward()
        ) {
            Text(">")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    Rf1Theme {
        val game = Game()
        val uiState = UiState()
        GameController(game, uiState).apply {
            applyMove(e2, e4)
            applyMove(e7, e5)
            applyMove(b1, c3)
            applyMove(b8, c6)
            applyMove(f1, b5)
            applyMove(d7, d5)
            applyMove(e4, d5)
            applyMove(d8, d5)
            applyMove(c3, d5)
        }
        uiState.apply {
            selectedPosition = g8
        }

        Game(
            game = game,
            uiState = uiState
        )
    }
}
