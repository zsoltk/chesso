package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.notation.Position

class GameController(
    private val game: Game,
    private val uiState: UiState
) {

    fun applyMove(from: Position, to: Position) {
        val currentState = game.states.last()
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
            capturedPieces = capturedPiece?.let { currentState.capturedPieces + it } ?: currentState.capturedPieces
        )

        game.states.remove(currentState)
        game.states.add(updatedCurrentState)
        game.states.add(newState)
        stepForward()
    }

    fun canStepBack(): Boolean =
        game.hasPrevIndex

    fun canStepForward(): Boolean =
        game.hasNextIndex

    fun stepForward() {
        if (canStepForward()) {
            game.currentIndex.value++
            uiState.selectedPosition.value = null
        }
    }

    fun stepBackward() {
        if (canStepBack()) {
            game.currentIndex.value--
            uiState.selectedPosition.value = null
        }
    }
}

