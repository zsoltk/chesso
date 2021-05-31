package com.github.zsoltk.rf1.ui.composable

import androidx.compose.runtime.Composable
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.game.preset.Preset

@Composable
fun Preset(preset: Preset) {
    Game(
        gameState = GameState(),
        preset = preset
    )
}
