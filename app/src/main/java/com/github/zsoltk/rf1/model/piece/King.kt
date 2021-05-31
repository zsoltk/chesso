package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.File.*
import com.github.zsoltk.rf1.model.game.state.GameSnaphotState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.KingSideCastle
import com.github.zsoltk.rf1.model.move.Move
import com.github.zsoltk.rf1.model.move.QueenSideCastle
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class King(override val set: Set) : Piece {

    override val value: Int = Int.MAX_VALUE

    override val symbol: String = when (set) {
        WHITE -> "♔"
        BLACK -> "♚"
    }

    override val textSymbol: String = "K"

    override fun pseudoLegalMoves(gameSnaphotState: GameSnaphotState, checkCheck: Boolean): List<BoardMove> {
        val moves = targets
            .mapNotNull { singleCaptureMove(gameSnaphotState, it.first, it.second) }
            .toMutableList()

        if (!checkCheck) {
            castleKingSide(gameSnaphotState)?.let { moves += it }
            castleQueenSide(gameSnaphotState)?.let { moves += it }
        }

        return moves
    }

    private fun castleKingSide(
        gameSnaphotState: GameSnaphotState
    ): BoardMove? {
        if (gameSnaphotState.hasCheck()) return null
        if (!gameSnaphotState.castlingInfo[set].canCastleKingSide) return null

        val rank = if (set == WHITE) 1 else 8
        val eSquare = gameSnaphotState.board[e, rank]!!
        val fSquare = gameSnaphotState.board[f, rank]!!
        val gSquare = gameSnaphotState.board[g, rank]!!
        val hSquare = gameSnaphotState.board[h, rank]!!
        if (fSquare.isNotEmpty || gSquare.isNotEmpty) return null
        if (gameSnaphotState.hasCheckFor(fSquare.position) || gameSnaphotState.hasCheckFor(gSquare.position)) return null

        return BoardMove(
            move = KingSideCastle(this, eSquare.position, gSquare.position),
            consequence = Move(Rook(set), hSquare.position, fSquare.position)
        )
    }

    private fun castleQueenSide(
        gameSnaphotState: GameSnaphotState
    ): BoardMove? {
        if (gameSnaphotState.hasCheck()) return null
        if (!gameSnaphotState.castlingInfo[set].canCastleQueenSide) return null

        val rank = if (set == WHITE) 1 else 8
        val eSquare = gameSnaphotState.board[e, rank]!!
        val dSquare = gameSnaphotState.board[d, rank]!!
        val cSquare = gameSnaphotState.board[c, rank]!!
        val bSquare = gameSnaphotState.board[b, rank]!!
        val aSquare = gameSnaphotState.board[a, rank]!!
        if (dSquare.isNotEmpty || cSquare.isNotEmpty || bSquare.isNotEmpty) return null
        if (gameSnaphotState.hasCheckFor(dSquare.position) || gameSnaphotState.hasCheckFor(cSquare.position)) return null

        return BoardMove(
            move = QueenSideCastle(this, eSquare.position, cSquare.position),
            consequence = Move(Rook(set), aSquare.position, dSquare.position)
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
