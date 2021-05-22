package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.zsoltk.rf1.model.game.Game
import com.github.zsoltk.rf1.model.game.UiState
import com.github.zsoltk.rf1.model.notation.Position.*
import com.github.zsoltk.rf1.ui.Rf1Theme
import kotlinx.coroutines.launch

@Composable
fun Game(game: Game = Game(), uiState: UiState = UiState()) {
    Column {
        ToMove(game)
        Moves(game)
        Board(
            gameState = game.states.last(),
            uiState = uiState,
            onMove = { from, to ->
                game.applyMove(from, to)
            }
        )
    }
}

@Composable
private fun ToMove(game: Game) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryVariant)
    ) {
        Text(
            text = "${game.states.last().toMove} to move",
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
private fun Moves(game: Game) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        // Where did you come from, where did you go?
        // Where did you come from ScrollableRow?
        LazyRow(
            state = listState,
            modifier = Modifier
                .padding(16.dp),
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

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    Rf1Theme {
        Game(
            game = Game().apply {
                applyMove(e2, e4)
                applyMove(e7, e5)
                applyMove(b1, c3)
                applyMove(b8, c6)
                applyMove(f1, b5)
                applyMove(d7, d5)
                applyMove(e4, d5)
            },
            uiState = UiState().apply {
                selectedPosition.value = d8
            }
        )
    }
}
