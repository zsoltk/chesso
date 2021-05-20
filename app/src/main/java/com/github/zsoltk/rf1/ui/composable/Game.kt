package com.github.zsoltk.rf1.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.zsoltk.rf1.model.game.Game
import com.github.zsoltk.rf1.model.game.UiState

@Composable
fun Game() {
    val game = remember { Game() }

    Board(game.states.last(), UiState())
}
