package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.File.*
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.KingSideCastle
import com.github.zsoltk.rf1.model.move.Move
import com.github.zsoltk.rf1.model.move.QueenSideCastle
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE
import kotlinx.parcelize.Parcelize

@Parcelize
class King(override val set: Set) : Piece {

    override val value: Int = Int.MAX_VALUE

    override val symbol: String = when (set) {
        WHITE -> "♔"
        BLACK -> "♚"
    }

    override val textSymbol: String = "K"

    override fun pseudoLegalMoves(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> {
        val moves = targets
            .mapNotNull { singleCaptureMove(gameSnapshotState, it.first, it.second) }
            .toMutableList()

        if (!checkCheck) {
            castleKingSide(gameSnapshotState)?.let { moves += it }
            castleQueenSide(gameSnapshotState)?.let { moves += it }
        }

        return moves
    }

    private fun castleKingSide(
        gameSnapshotState: GameSnapshotState
    ): BoardMove? {
        if (gameSnapshotState.hasCheck()) return null
        if (!gameSnapshotState.castlingInfo[set].canCastleKingSide) return null

        val rank = if (set == WHITE) 1 else 8
        val eSquare = gameSnapshotState.board[e, rank]!!
        val fSquare = gameSnapshotState.board[f, rank]!!
        val gSquare = gameSnapshotState.board[g, rank]!!
        val hSquare = gameSnapshotState.board[h, rank]!!
        if (fSquare.isNotEmpty || gSquare.isNotEmpty) return null
        if (gameSnapshotState.hasCheckFor(fSquare.position) || gameSnapshotState.hasCheckFor(gSquare.position)) return null

        return BoardMove(
            move = KingSideCastle(this, eSquare.position, gSquare.position),
            consequence = Move(hSquare.piece!!, hSquare.position, fSquare.position)
        )
    }

    private fun castleQueenSide(
        gameSnapshotState: GameSnapshotState
    ): BoardMove? {
        if (gameSnapshotState.hasCheck()) return null
        if (!gameSnapshotState.castlingInfo[set].canCastleQueenSide) return null

        val rank = if (set == WHITE) 1 else 8
        val eSquare = gameSnapshotState.board[e, rank]!!
        val dSquare = gameSnapshotState.board[d, rank]!!
        val cSquare = gameSnapshotState.board[c, rank]!!
        val bSquare = gameSnapshotState.board[b, rank]!!
        val aSquare = gameSnapshotState.board[a, rank]!!
        if (dSquare.isNotEmpty || cSquare.isNotEmpty || bSquare.isNotEmpty) return null
        if (gameSnapshotState.hasCheckFor(dSquare.position) || gameSnapshotState.hasCheckFor(cSquare.position)) return null

        return BoardMove(
            move = QueenSideCastle(this, eSquare.position, cSquare.position),
            consequence = Move(aSquare.piece!!, aSquare.position, dSquare.position)
        )
    }

    companion object {
        val targets = listOf(
            -1 to -1,
            -1 to 0,
            -1 to 1,
            0 to 1,
            0 to -1,
            1 to -1,
            1 to 0,
            1 to 1,
        )
    }
}
