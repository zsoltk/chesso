package com.github.zsoltk.rf1.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.rf1.model.game.Game
import com.github.zsoltk.rf1.model.game.UiState
import com.github.zsoltk.rf1.model.notation.Position.*
import com.github.zsoltk.rf1.ui.Rf1Theme

@Composable
fun Game(game: Game = Game(), uiState: UiState = UiState()) {
    Board(
        gameState = game.states.last(),
        uiState = uiState,
        onMove = { from, to ->
            game.applyMove(from, to)
        }
    )
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
