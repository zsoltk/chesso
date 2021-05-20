package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.mutableStateListOf
import com.github.zsoltk.rf1.model.notation.Position

class Game {

    val states = mutableStateListOf(GameState())

    fun applyMove(from: Position, to: Position) {
        val currentState = states.last()
        val board = currentState.board
        val piece = board[from].piece

        val newState = currentState.copy(
            toMove = currentState.toMove.opposite(),
            lastMove = from to to,
            board = board.copy(
                pieces = board.pieces
                    .minus(from)
                    .plus(to to piece)
            )
        )

        states.add(newState)
    }
}

