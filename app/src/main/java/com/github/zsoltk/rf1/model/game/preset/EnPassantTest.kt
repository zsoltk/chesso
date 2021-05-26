package com.github.zsoltk.rf1.model.game.preset

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.GameController
import com.github.zsoltk.rf1.ui.Rf1Theme
import com.github.zsoltk.rf1.ui.composable.Preset

object EnPassantTest : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            applyMove(Position.e2, Position.e4)
            applyMove(Position.b8, Position.c6)
            applyMove(Position.e4, Position.e5)
            applyMove(Position.d7, Position.d5)
            onClick(Position.e5)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnPassantTestPreview() {
    Rf1Theme {
        Preset(EnPassantTest)
    }
}

