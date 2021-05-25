package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.zsoltk.rf1.model.move.CalculatedMove
import com.github.zsoltk.rf1.model.game.Game
import com.github.zsoltk.rf1.model.game.GameController
import com.github.zsoltk.rf1.model.game.state.UiState
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.ui.Rf1Theme
import kotlinx.coroutines.launch

@Composable
fun Moves(game: Game) {
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
            moves.forEachIndexed { index, move ->
                if (index % 2 == 0) {
                    item {
                        StepNumber(index / 2 + 1)
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }

                item {
                    if (index == game.currentIndex - 1) {
                        Pill { Move(move) }
                    } else {
                        Move(move)
                    }
                }

                if (index % 2 == 1) {
                    item { Spacer(modifier = Modifier.width(10.dp)) }
                }
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
private fun StepNumber(stepNumber: Int) {
    Text(
        text = "$stepNumber.",
        color = MaterialTheme.colors.onSecondary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun Pill(
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.background(Color.Gray, RoundedCornerShape(6.dp))) {
        content()
    }
}

@Composable
private fun Move(
    move: CalculatedMove
) {
    Text(
        text = move.toString(),
        color = MaterialTheme.colors.onSecondary,
        modifier = Modifier.padding(start = 3.dp, end = 3.dp)
    )
}

@Preview
@Composable
fun MovesPreview() {
    Rf1Theme {
        val game = Game()
        val uiState = UiState()
        GameController(game, uiState).apply {
            applyMove(Position.e2, Position.e4)
            applyMove(Position.e7, Position.e5)
            applyMove(Position.b1, Position.c3)
            applyMove(Position.b8, Position.c6)
            applyMove(Position.f1, Position.b5)
            applyMove(Position.d7, Position.d5)
            applyMove(Position.e4, Position.d5)
            applyMove(Position.d8, Position.d5)
            applyMove(Position.c3, Position.d5)
            stepBackward()
            stepBackward()
            stepBackward()
            stepBackward()
        }

        Moves(
            game = game
        )
    }
}
