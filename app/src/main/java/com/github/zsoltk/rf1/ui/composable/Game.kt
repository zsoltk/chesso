package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.zsoltk.rf1.model.game.Game
import com.github.zsoltk.rf1.model.game.UiState
import com.github.zsoltk.rf1.model.notation.Position.*
import com.github.zsoltk.rf1.ui.Rf1Theme

@Composable
fun Game(game: Game = Game(), uiState: UiState = UiState()) {
    Column {
        ToMove(game)
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

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    Rf1Theme {
        Game(
            game = Game().apply {
                applyMove(e2, e4)
                applyMove(d7, d5)
            },
            uiState = UiState().apply {
                selectedPosition.value = e4
            }
        )
    }
}
