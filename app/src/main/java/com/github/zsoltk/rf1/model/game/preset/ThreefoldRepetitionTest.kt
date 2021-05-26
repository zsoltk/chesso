package com.github.zsoltk.rf1.model.game.preset

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.game.GameController
import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Queen
import com.github.zsoltk.rf1.model.piece.Rook
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.ui.Rf1Theme
import com.github.zsoltk.rf1.ui.composable.Preset

object ThreefoldRepetitionTest : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    a7 to King(Set.BLACK),
                    e8 to Rook(Set.WHITE),
                    d5 to Queen(Set.WHITE),
                    g2 to King(Set.WHITE),
                )
            )
            val boardState = BoardState(
                board = board,
                toMove = Set.WHITE
            )
            reset(GameState(boardState))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThreefoldRepetitionTestPreview() {
    Rf1Theme {
        Preset(ThreefoldRepetitionTest)
    }
}

