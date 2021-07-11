package com.github.zsoltk.chesso.ui.composable

import androidx.compose.runtime.Composable
import com.github.zsoltk.chesso.model.game.preset.Preset
import com.github.zsoltk.chesso.model.game.state.GamePlayState

@Composable
fun Preset(preset: Preset) {
    Game(
        state = GamePlayState(),
        preset = preset
    )
}
