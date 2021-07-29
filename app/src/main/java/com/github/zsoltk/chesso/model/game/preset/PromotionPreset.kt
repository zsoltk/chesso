package com.github.zsoltk.chesso.model.game.preset

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.chesso.model.board.Board
import com.github.zsoltk.chesso.model.board.Position.*
import com.github.zsoltk.chesso.model.game.controller.GameController
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.piece.King
import com.github.zsoltk.chesso.model.piece.Knight
import com.github.zsoltk.chesso.model.piece.Pawn
import com.github.zsoltk.chesso.model.piece.Set.BLACK
import com.github.zsoltk.chesso.model.piece.Set.WHITE
import com.github.zsoltk.chesso.ui.base.ChessoTheme
import com.github.zsoltk.chesso.ui.app.Preset

object PromotionPreset : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    a7 to King(BLACK),
                    f8 to Knight(BLACK),
                    g2 to King(WHITE),
                    g7 to Pawn(WHITE),
                )
            )
            reset(
                GameSnapshotState(
                    board = board,
                    toMove = WHITE
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PromotionPresetPreview() {
    ChessoTheme {
        Preset(PromotionPreset)
    }
}

