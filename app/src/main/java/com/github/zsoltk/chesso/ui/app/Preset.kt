package com.github.zsoltk.chesso.ui.app

import androidx.compose.runtime.Composable
import com.github.zsoltk.chesso.model.game.preset.Preset
import com.github.zsoltk.chesso.model.game.state.GamePlayState
import com.github.zsoltk.chesso.ui.chess.Game

@Composable
fun Preset(preset: Preset) {
    Game(
        state = GamePlayState(),
        preset = preset
    )
}
