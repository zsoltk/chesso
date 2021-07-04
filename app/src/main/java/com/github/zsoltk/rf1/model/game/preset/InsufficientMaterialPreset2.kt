package com.github.zsoltk.rf1.model.game.preset

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.piece.Bishop
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Rook
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE
import com.github.zsoltk.rf1.ui.Rf1Theme
import com.github.zsoltk.rf1.ui.composable.Preset

object InsufficientMaterialPreset2 : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    a7 to King(BLACK),
                    g8 to Rook(BLACK),
                    g2 to King(WHITE),
                    d5 to Bishop(WHITE),
                    c2 to Bishop(BLACK),
                )
            )
            val boardState = BoardState(
                board = board,
                toMove = WHITE
            )
            reset(GameSnapshotState(boardState))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsufficientMaterialPreset2Preview() {
    Rf1Theme {
        Preset(InsufficientMaterialPreset2)
    }
}

