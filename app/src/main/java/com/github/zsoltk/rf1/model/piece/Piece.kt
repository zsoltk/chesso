package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.GameState
import com.github.zsoltk.rf1.model.game.Move

interface Piece {

    val set: Set

    val symbol: String

    val value: Int

    fun moves(gameState: GameState): List<Move> = emptyList()

    fun attacks(gameState: GameState): List<Move> = moves(gameState)

    fun movesWithoutCaptures(gameState: GameState): List<Move> =
        moves(gameState) - possibleCaptures(gameState)

    fun possibleCaptures(gameState: GameState): List<Move> =
        attacks(gameState)
            .filter { move ->
                gameState.board[move.to].hasPiece(set.opposite())
            }
}
