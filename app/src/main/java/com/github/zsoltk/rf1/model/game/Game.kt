package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.mutableStateListOf
import com.github.zsoltk.rf1.model.notation.Position

class Game {

    val states = mutableStateListOf(GameState())

    fun applyMove(from: Position, to: Position) {
        val currentState = states.last()
        val board = currentState.board
        val piece = board[from].piece
        requireNotNull(piece)

        val move = Move(
            from = from,
            to = to,
            piece = piece,
            isCapture = board.pieces[to] != null
        )

        val updatedCurrentState = currentState.copy(
            move = move
        )

        val newState = currentState.copy(
            toMove = currentState.toMove.opposite(),
            lastMove = move,
            board = board.copy(
                pieces = board.pieces
                    .minus(from)
                    .plus(to to piece)
            )
        )

        states.remove(currentState)
        states.add(updatedCurrentState)
        states.add(newState)
    }

    fun moves(): String {
        val moves = StringBuilder()
        states.forEachIndexed { index, gameState ->
            gameState.move?.let {
                if (index % 2 == 0) {
                    if (index > 0) moves.append("  ")
                    moves.append("${(index / 2 + 1)}. ")
                }
                moves.append(it)
                moves.append(" ")
            }
        }

        return moves.toString()
    }
}

