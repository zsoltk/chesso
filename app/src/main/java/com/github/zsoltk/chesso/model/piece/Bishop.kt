package com.github.zsoltk.chesso.model.piece

import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.move.BoardMove
import com.github.zsoltk.chesso.model.piece.Set.BLACK
import com.github.zsoltk.chesso.model.piece.Set.WHITE
import kotlinx.parcelize.Parcelize

@Parcelize
class Bishop(override val set: Set) : Piece {

    override val value: Int = 3

    override val asset: Int =
        when (set) {
            WHITE -> R.drawable.bishop_light
            BLACK -> R.drawable.bishop_dark
        }

    override val symbol: String = when (set) {
        WHITE -> "♗"
        BLACK -> "♝"
    }

    override val textSymbol: String = "B"

    override fun pseudoLegalMoves(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> =
        lineMoves(gameSnapshotState, directions)

    companion object {
        val directions = listOf(
            -1 to -1,
            -1 to 1,
            1 to -1,
            1 to 1,
        )
    }
}
