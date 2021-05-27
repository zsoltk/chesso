package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.File.*
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.Move
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class King(override val set: Set) : Piece {

    override val value: Int = Int.MAX_VALUE

    override val symbol: String = when (set) {
        WHITE -> "♔"
        BLACK -> "♚"
    }

    override val textSymbol: String = "K"

    override fun pseudoLegalMoves(gameState: GameState, checkCheck: Boolean): List<BoardMove> {
        val moves = targets
            .mapNotNull { singleCaptureMove(gameState, it.first, it.second) }
            .toMutableList()

        if (!checkCheck) {
            castleKingSide(gameState)?.let { moves += it }
            castleQueenSide(gameState)?.let { moves += it }
        }

        return moves
    }

    private fun castleKingSide(
        gameState: GameState
    ): BoardMove? {
        if (gameState.hasCheck()) return null
        if (!gameState.castlingInfo[set].canCastleKingSide) return null

        val rank = if (set == WHITE) 1 else 8
        val eSquare = gameState.board[e, rank]!!
        val fSquare = gameState.board[f, rank]!!
        val gSquare = gameState.board[g, rank]!!
        val hSquare = gameState.board[h, rank]!!
        if (fSquare.isNotEmpty || gSquare.isNotEmpty) return null
        if (gameState.hasCheckFor(fSquare.position) || gameState.hasCheckFor(gSquare.position)) return null

        return BoardMove(
            move = Move(this, eSquare.position, gSquare.position),
            consequence = Move(Rook(set), hSquare.position, fSquare.position)
        )
    }

    private fun castleQueenSide(
        gameState: GameState
    ): BoardMove? {
        if (gameState.hasCheck()) return null
        if (!gameState.castlingInfo[set].canCastleQueenSide) return null

        val rank = if (set == WHITE) 1 else 8
        val eSquare = gameState.board[e, rank]!!
        val dSquare = gameState.board[d, rank]!!
        val cSquare = gameState.board[c, rank]!!
        val bSquare = gameState.board[b, rank]!!
        val aSquare = gameState.board[a, rank]!!
        if (dSquare.isNotEmpty || cSquare.isNotEmpty || bSquare.isNotEmpty) return null
        if (gameState.hasCheckFor(dSquare.position) || gameState.hasCheckFor(cSquare.position)) return null

        return BoardMove(
            move = Move(this, eSquare.position, cSquare.position),
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
