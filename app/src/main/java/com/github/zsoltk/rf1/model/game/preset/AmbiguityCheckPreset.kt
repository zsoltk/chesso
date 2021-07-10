package com.github.zsoltk.rf1.model.game.preset

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Knight
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Queen
import com.github.zsoltk.rf1.model.piece.Rook
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE
import com.github.zsoltk.rf1.ui.Rf1Theme
import com.github.zsoltk.rf1.ui.composable.Preset

object AmbiguityCheckPreset : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    e2 to King(BLACK),
                    c3 to Pawn(BLACK),
                    h1 to King(WHITE),
                    e4 to Knight(WHITE),
                    a4 to Knight(WHITE),
                    a2 to Knight(WHITE),
                    b1 to Knight(WHITE),
                    d1 to Knight(WHITE),
                    b7 to Rook(WHITE),
                    c8 to Rook(WHITE),
                    d7 to Rook(WHITE),
                    g8 to Queen(WHITE),
                    h8 to Queen(WHITE),
                    h7 to Queen(WHITE),
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
fun AmbiguityCheckPresetPreview() {
    Rf1Theme {
        Preset(AmbiguityCheckPreset)
    }
}

