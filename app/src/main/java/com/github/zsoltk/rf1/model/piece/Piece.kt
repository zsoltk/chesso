package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.move.BoardMove

interface Piece {

    val set: Set

    val symbol: String

    val value: Int

    fun moves(boardState: BoardState): List<BoardMove> = emptyList()

    fun attacks(boardState: BoardState): List<BoardMove> = moves(boardState)

    fun movesWithoutCaptures(boardState: BoardState): List<BoardMove> =
        moves(boardState) - possibleCaptures(boardState)

    fun possibleCaptures(boardState: BoardState): List<BoardMove> =
        attacks(boardState)
            .filter { move ->
                boardState.board[move.to].hasPiece(set.opposite())
            }
}
