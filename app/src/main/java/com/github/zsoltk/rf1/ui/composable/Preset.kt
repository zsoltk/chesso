package com.github.zsoltk.rf1.ui.composable

import androidx.compose.runtime.Composable
import com.github.zsoltk.rf1.model.game.preset.Preset
import com.github.zsoltk.rf1.model.game.state.GamePlayState

@Composable
fun Preset(preset: Preset) {
    Game(
        state = GamePlayState(),
        preset = preset
    )
}
