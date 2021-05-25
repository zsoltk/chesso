package com.github.zsoltk.rf1.model.game.preset

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.GameController
import com.github.zsoltk.rf1.ui.Rf1Theme
import com.github.zsoltk.rf1.ui.composable.Preset

object CheckMateTest : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            applyMove(Position.e2, Position.e4)
            applyMove(Position.e7, Position.e5)
            applyMove(Position.d1, Position.h5)
            applyMove(Position.b8, Position.c6)
            applyMove(Position.f1, Position.c4)
            applyMove(Position.f8, Position.c5)
            onClick(Position.h5)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckMateTestPreview() {
    Rf1Theme {
        Preset(ThreefoldRepetitionTest)
    }
}

