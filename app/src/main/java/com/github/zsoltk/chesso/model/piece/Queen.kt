package com.github.zsoltk.chesso.model.piece

import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.move.BoardMove
import com.github.zsoltk.chesso.model.piece.Set.BLACK
import com.github.zsoltk.chesso.model.piece.Set.WHITE
import kotlinx.parcelize.Parcelize

@Parcelize
class Queen(override val set: Set) : Piece {

    override val value: Int = 9

    override val asset: Int =
        when (set) {
            WHITE -> R.drawable.queen_light
            BLACK -> R.drawable.queen_dark
        }

    override val symbol: String = when (set) {
        WHITE -> "♕"
        BLACK -> "♛"
    }

    override val textSymbol: String = "Q"

    override fun pseudoLegalMoves(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> =
        lineMoves(gameSnapshotState, Rook.directions + Bishop.directions)
}
