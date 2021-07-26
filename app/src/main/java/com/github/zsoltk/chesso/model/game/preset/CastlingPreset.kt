package com.github.zsoltk.chesso.model.game.preset

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.chesso.model.board.Position.*
import com.github.zsoltk.chesso.model.game.controller.GameController
import com.github.zsoltk.chesso.ui.base.ChessoTheme
import com.github.zsoltk.chesso.ui.app.Preset

object CastlingPreset : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            reset()
            applyMove(e2, e4)
            applyMove(e7, e5)

            applyMove(d2, d4)
            applyMove(d7, d5)

            applyMove(b1, c3)
            applyMove(b8, c6)

            applyMove(g1, f3)
            applyMove(g8, f6)

            applyMove(c1, e3)
            applyMove(c8, e6)

            applyMove(f1, d3)
            applyMove(f8, d6)

            applyMove(d1, d2)
            applyMove(d8, d7)
            onClick(e1)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CastlingPresetPreview() {
    ChessoTheme {
        Preset(CastlingPreset)
    }
}

