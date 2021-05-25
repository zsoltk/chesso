package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.notation.Position

class GameController(
    private val game: Game,
    private val uiState: UiState
) {

    private val gameState: GameState
        get() = game.currentState

    private val boardState: BoardState
        get() = gameState.boardState

    fun square(position: Position): Square =
        boardState.board[position]

    fun highlightedPositions(): List<Position> =
        lastMovePositions() + uiSelectedPositions()

    private fun lastMovePositions(): List<Position> =
        gameState.lastMove?.let { listOf(it.from, it.to) } ?: emptyList()

    private fun uiSelectedPositions(): List<Position> =
        uiState.selectedPosition?.let { listOf(it) } ?: emptyList()

    fun clickablePositions(): List<Position> =
        ownPiecePositions() +
            possibleCapturesFromSelectedPosition().targetPositions() +
            possibleMovesFromSelectedPosition().targetPositions()

    private fun ownPiecePositions(): List<Position> =
        boardState.board.pieces
            .filter { (position, _) -> position.hasOwnPiece() }
            .map { it.key }

    fun possibleCapturesFromSelectedPosition() =
        boardState.legalCapturesFrom(uiState.selectedPosition)

    fun possibleMovesFromSelectedPosition(): List<Move> =
        boardState.legalMovesFrom(uiState.selectedPosition)

    private fun Position.hasOwnPiece() =
        square(this).hasPiece(boardState.toMove)

    fun onClick(position: Position) {
        if (position.hasOwnPiece()) {
            selectPosition(position)
        } else if (canMoveTo(position) || canCaptureAt(position)) {
            val selectedPosition = uiState.selectedPosition
            requireNotNull(selectedPosition)
            applyMove(selectedPosition, position)
        }
    }

    private fun selectPosition(position: Position) {
        if (uiState.selectedPosition == position) {
            uiState.selectedPosition = null
        } else {
            uiState.selectedPosition = position
        }
    }

    private fun canMoveTo(position: Position) =
        position in boardState.legalMovesFrom(uiState.selectedPosition).targetPositions()

    private fun canCaptureAt(position: Position) =
        position in boardState.legalCapturesFrom(uiState.selectedPosition).targetPositions()

    fun applyMove(from: Position, to: Position) {
        var states = game.states.toMutableList()
        val currentIndex = game.currentIndex
        val moveIntention = MoveIntention(from, to)
        val appliedMove = gameState.calculateAppliedMove(moveIntention)

        states[currentIndex] = appliedMove.updatedCurrentState
        states = states.subList(0, currentIndex + 1)
        game.currentIndex = states.lastIndex
        game.states = states + appliedMove.newState
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

