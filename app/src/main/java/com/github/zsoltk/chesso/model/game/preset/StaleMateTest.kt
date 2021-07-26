package com.github.zsoltk.chesso.model.game.preset

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.chesso.model.board.Board
import com.github.zsoltk.chesso.model.board.Position.*
import com.github.zsoltk.chesso.model.game.controller.GameController
import com.github.zsoltk.chesso.model.game.state.BoardState
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.piece.King
import com.github.zsoltk.chesso.model.piece.Queen
import com.github.zsoltk.chesso.model.piece.Rook
import com.github.zsoltk.chesso.model.piece.Set.BLACK
import com.github.zsoltk.chesso.model.piece.Set.WHITE
import com.github.zsoltk.chesso.ui.base.ChessoTheme
import com.github.zsoltk.chesso.ui.app.Preset

object StaleMatePreset : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    a7 to King(BLACK),
                    e8 to Rook(WHITE),
                    d5 to Queen(WHITE),
                    g2 to King(WHITE),
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
fun StaleMatePresetPreview() {
    ChessoTheme {
        Preset(StaleMatePreset)
    }
}

