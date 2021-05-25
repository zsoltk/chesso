package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.BoardState
import com.github.zsoltk.rf1.model.move.Move

interface Piece {

    val set: Set

    val symbol: String

    val value: Int

    fun moves(boardState: BoardState): List<Move> = emptyList()

    fun attacks(boardState: BoardState): List<Move> = moves(boardState)

    fun movesWithoutCaptures(boardState: BoardState): List<Move> =
        moves(boardState) - possibleCaptures(boardState)

    fun possibleCaptures(boardState: BoardState): List<Move> =
        attacks(boardState)
            .filter { move ->
                boardState.board[move.to].hasPiece(set.opposite())
            }
}
