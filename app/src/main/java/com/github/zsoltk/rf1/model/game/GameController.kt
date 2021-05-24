package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set

class GameController(
    private val game: Game,
    private val uiState: UiState
) {

    private val gameState: GameState
        get() = game.currentState

    fun square(position: Position): Square =
        gameState.board[position]

    fun highlightedPositions(): List<Position> =
        lastMovePositions() + uiSelectedPositions()

    private fun lastMovePositions(): List<Position> =
        gameState.lastMove?.let { listOf(it.from, it.to) } ?: emptyList()

    private fun uiSelectedPositions(): List<Position> =
        uiState.selectedPosition?.let { listOf(it) } ?: emptyList()

    fun clickablePositions(): List<Position> =
        ownPieces() +
            possibleMovesFromSelectedPosition().targetPositions() +
            possibleCapturesFromSelectedPosition().targetPositions()

    fun ownPieces(): List<Position> =
        gameState.board.pieces
            .filter { (position, _) -> position.hasOwnPiece() }
            .map { it.key }

    private fun Position.hasOwnPiece() =
        square(this).hasPiece(gameState.toMove)

    fun onClick(position: Position) {
        if (position.hasOwnPiece()) {
            selectPosition(position)
        } else if (position in possibleMovesFromSelectedPosition().targetPositions() || position in possibleCapturesFromSelectedPosition().targetPositions()) {
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

    fun possibleMovesFromSelectedPosition(): List<Move> =
        uiState.selectedPosition.let { position ->
            position
                .possibleMovesWithoutCaptures()
                .applyCheckConstraints()
        }

    fun possibleCapturesFromSelectedPosition(): List<Move> =
        uiState.selectedPosition.let { position ->
            position
                .possibleCaptures()
                .applyCheckConstraints()
        }

    private fun Position?.possibleMovesWithoutCaptures(): List<Move> =
        map { piece ->
            piece.movesWithoutCaptures(gameState)
        }

    private fun Position?.possibleCaptures(): List<Move> =
        map { piece ->
            piece.possibleCaptures(gameState)
        }

    private fun Position?.map(mapper: (Piece) -> List<Move>): List<Move> {
        var list = emptyList<Move>()

        this?.let { nonNullPosition ->
            val square = gameState.board[nonNullPosition]
            square.piece?.let { piece ->
                list = mapper(piece)
            }
        }

        return list
    }

    private fun List<Move>.applyCheckConstraints(): List<Move> =
        filter { move ->
            val calculatedMove = calculateMove(move.from, move.to)
            !calculatedMove.newState.hasCheckFor(gameState.toMove)
        }

    fun GameState.hasCheckFor(set: Set): Boolean {
        val kingsPosition: Position? = board.pieces.keys.find { position ->
            val piece = board.pieces[position]
            piece is King && piece.set == set
        }

        return board.pieces.any { (_, piece) ->
            val otherPieceCaptures: List<Move> = piece?.possibleCaptures(this) ?: emptyList()
            kingsPosition in otherPieceCaptures.targetPositions()
        }
    }

    fun applyMove(from: Position, to: Position) {
        var states = game.states.toMutableList()
        val currentIndex = game.currentIndex
        val calculatedMove = calculateMove(from, to)

        states[currentIndex] = calculatedMove.updatedCurrentState
        states = states.subList(0, currentIndex + 1)
        game.currentIndex = states.lastIndex
        game.states = states + calculatedMove.newState
        stepForward()
    }

    private fun calculateMove(from: Position, to: Position): CalculatedMove {
        val currentState = game.currentState
        val board = currentState.board
        val piece = board[from].piece
        val capturedPiece = board[to].piece
        requireNotNull(piece)

        val updatedBoard = board.copy(
            pieces = board.pieces
                .minus(from)
                .plus(to to piece)
        )

        val newState = currentState.copy(
            board = updatedBoard,
            toMove = currentState.toMove.opposite(),
            lastMove = null, // is updated in next step after evaluating check
            move = null,
            capturedPieces = capturedPiece?.let { currentState.capturedPieces + it }
                ?: currentState.capturedPieces
        )

        val move = Move(
            from = from,
            to = to,
            piece = piece,
            isCapture = board.pieces[to] != null,
            isCheck = newState.hasCheckFor(currentState.toMove.opposite())
        )

        val updatedCurrentState = currentState.copy(
            move = move
        )

        val newStateWithMove = newState.copy(
            lastMove = move,
        )
        return CalculatedMove(
            move = move,
            updatedCurrentState = updatedCurrentState,
            newState = newStateWithMove
        )
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

