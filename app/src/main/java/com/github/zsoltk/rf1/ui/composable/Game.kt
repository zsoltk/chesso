package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.zsoltk.rf1.model.game.Game
import com.github.zsoltk.rf1.model.game.GameController
import com.github.zsoltk.rf1.model.game.UiState
import com.github.zsoltk.rf1.model.notation.Position.*
import com.github.zsoltk.rf1.ui.Rf1Theme
import kotlinx.coroutines.launch

@Composable
fun Game(game: Game = Game(), uiState: UiState = UiState()) {
    val gameController = remember { GameController(game, uiState) }

    Column {
        ToMove(game)
        Moves(game)
        CapturedPieces(game)
        Board(
            gameState = game.currentState,
            uiState = uiState,
            onMove = { from, to ->
                gameController.applyMove(from, to)
            }
        )
        Spacer(modifier = Modifier.height(48.dp))
        TimeTravelButtons(gameController)
    }
}

@Composable
private fun ToMove(game: Game) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(MaterialTheme.colors.primaryVariant),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${game.states.last().toMove} to move",
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
private fun Moves(game: Game) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.CenterStart
    ) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        // Where did you come from, where did you go?
        // Where did you come from ScrollableRow?
        LazyRow(
            state = listState,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val moves = game.moves()
            items(moves.size) { index ->
                Text(
                    text = moves[index],
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            if (moves.isNotEmpty()) {
                coroutineScope.launch {
                    listState.animateScrollToItem(moves.lastIndex)
                }
            }
        }
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
            selectedPosition.value = g8
        }

        Game(
            game = game,
            uiState = uiState
        )
    }
}
