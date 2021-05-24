package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.GameState
import com.github.zsoltk.rf1.model.notation.Position

interface Piece {

    val set: Set

    val symbol: String

    val value: Int

    fun moves(gameState: GameState): List<Position> = emptyList()

    fun attacks(gameState: GameState): List<Position> = moves(gameState)

    fun movesWithoutCaptures(gameState: GameState) =
        moves(gameState) - possibleCaptures(gameState)

    fun possibleCaptures(gameState: GameState): List<Position> =
        attacks(gameState)
            .filter {
                gameState.board[it].hasPiece(gameState.toMove.opposite())
            }
}
