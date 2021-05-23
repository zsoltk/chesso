package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.notation.Position

class GameController(
    private val game: Game,
    private val uiState: UiState
) {

    fun applyMove(from: Position, to: Position) {
        var states = game.states.toMutableList()
        val currentIndex = game.currentIndex
        val currentState = game.currentState
        val board = currentState.board
        val piece = board[from].piece
        val capturedPiece = board[to].piece
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
            board = board.copy(
                pieces = board.pieces
                    .minus(from)
                    .plus(to to piece)
            ),
            toMove = currentState.toMove.opposite(),
            lastMove = move,
            move = null,
            capturedPieces = capturedPiece?.let { currentState.capturedPieces + it } ?: currentState.capturedPieces
        )


        states[currentIndex] = updatedCurrentState
        states = states.subList(0, currentIndex + 1)
        game.currentIndex = states.lastIndex
        game.states = states + newState
        stepForward()
    }

    fun canStepBack(): Boolean =
        game.hasPrevIndex

    fun canStepForward(): Boolean =
        game.hasNextIndex

    fun stepForward() {
        if (canStepForward()) {
            game.currentIndex++
            uiState.selectedPosition = null
        }
    }

    fun stepBackward() {
        if (canStepBack()) {
            game.currentIndex--
            uiState.selectedPosition = null
        }
    }
}

