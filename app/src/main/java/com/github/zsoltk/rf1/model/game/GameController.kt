package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.notation.Position

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
        ownPiecePositions() +
            possibleCapturesFromSelectedPosition().targetPositions() +
            possibleMovesFromSelectedPosition().targetPositions()

    private fun ownPiecePositions(): List<Position> =
        gameState.board.pieces
            .filter { (position, _) -> position.hasOwnPiece() }
            .map { it.key }

    fun possibleCapturesFromSelectedPosition() =
        gameState.legalCapturesFrom(uiState.selectedPosition)

    fun possibleMovesFromSelectedPosition(): List<Move> =
        gameState.legalMovesFrom(uiState.selectedPosition)

    private fun Position.hasOwnPiece() =
        square(this).hasPiece(gameState.toMove)

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
        position in gameState.legalMovesFrom(uiState.selectedPosition).targetPositions()

    private fun canCaptureAt(position: Position) =
        position in gameState.legalCapturesFrom(uiState.selectedPosition).targetPositions()

//    fun GameState.possibleMovesFrom(from: Position?): List<Move> =
//        from.let { position ->
//            possibleMovesWithoutCaptures(position).applyCheckConstraints(this)
//        }
//
//    fun GameState.possibleCapturesFrom(from: Position?): List<Move> =
//        from.let { position ->
//            possibleCaptures(position).applyCheckConstraints(this)
//        }
//
//    private fun GameState.possibleMovesWithoutCaptures(from: Position?): List<Move> =
//        map(from) { piece ->
//            piece.movesWithoutCaptures(gameState)
//        }
//
//    private fun GameState.possibleCaptures(from: Position?): List<Move> =
//        map(from) { piece ->
//            piece.possibleCaptures(gameState)
//        }
//
//    private fun GameState.map(from: Position?, mapper: (Piece) -> List<Move>): List<Move> {
//        var list = emptyList<Move>()
//
//        from?.let { nonNullPosition ->
//            val square = board[nonNullPosition]
//            square.piece?.let { piece ->
//                list = mapper(piece)
//            }
//        }
//
//        return list
//    }
//
//    private fun List<Move>.applyCheckConstraints(gameState: GameState): List<Move> =
//        filter { move ->
//            val calculatedMove = calculateMovePartial(move.from, move.to)
//            !calculatedMove.newState.hasCheckFor(gameState.toMove)
//        }

    fun applyMove(from: Position, to: Position) {
        var states = game.states.toMutableList()
        val currentIndex = game.currentIndex
        val calculatedMove = gameState.calculateMove(from, to)

        states[currentIndex] = calculatedMove.updatedCurrentState
        states = states.subList(0, currentIndex + 1)
        game.currentIndex = states.lastIndex
        game.states = states + calculatedMove.newState
        stepForward()
    }

//    private fun calculateMoveFull(from: Position, to: Position): CalculatedMove {
//        val partial = calculateMovePartial(from, to)
//        val newState = partial.newState
//        val nextToMove = newState.toMove
//
//        Log.d("Chess", "---")
//        val thereAreValidMoves = newState.board.pieces(nextToMove).filter { (position, _) ->
//            Log.d("Chess", "-")
//            val possibleMovesFrom = newState.possibleMovesFrom(position)
//            val possibleCapturesFrom = newState.possibleCapturesFrom(position)
//            if (possibleMovesFrom.isNotEmpty() ) {
//                Log.d("Chess", "Possible moves: $possibleMovesFrom")
//            }
//            if (possibleCapturesFrom.isNotEmpty() ) {
//                Log.d("Chess", "Possible captures: $possibleCapturesFrom")
//            }
//            newState.possibleMovesFrom(position).isNotEmpty() || newState.possibleCapturesFrom(position).isNotEmpty()
//        }
//        val isCheckMate = partial.move.isCheck == true && thereAreValidMoves.isEmpty()
//        val move = partial.move.copy(
//            isCheckMate = isCheckMate
//        )
//
//        return partial.copy(
//            move = move,
//            updatedCurrentState = partial.updatedCurrentState.copy(
//                move = move
//            ),
//            newState = newState.copy(
//                lastMove = move,
//                resolution = if (isCheckMate) CHECKMATE else IN_PROGRESS
//            )
//        )
//    }

//    private fun calculateMovePartial(from: Position, to: Position): CalculatedMove {
//        val currentState = game.currentState
//        val board = currentState.board
//        val piece = board[from].piece
//        val capturedPiece = board[to].piece
//        requireNotNull(piece)
//
//        val updatedBoard = board.copy(
//            pieces = board.pieces
//                .minus(from)
//                .plus(to to piece)
//        )
//
//        val nextToMove = currentState.toMove.opposite()
//        val newState = currentState.copy(
//            board = updatedBoard,
//            toMove = nextToMove,
//            lastMove = null, // is updated in next step after evaluating check
//            move = null,
//            capturedPieces = capturedPiece?.let { currentState.capturedPieces + it }
//                ?: currentState.capturedPieces
//        )
//
//        val move = Move(
//            from = from,
//            to = to,
//            piece = piece,
//            isCapture = board.pieces[to] != null,
//            isCheck = currentState.hasCheckFor(nextToMove)
//        )
//
//        val updatedCurrentState = currentState.copy(
//            move = move
//        )
//
//        val newStateWithMove = newState.copy(
//            lastMove = move,
//        )
//
//        return CalculatedMove(
//            move = move,
//            updatedCurrentState = updatedCurrentState,
//            newState = newStateWithMove
//        )
//    }

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

