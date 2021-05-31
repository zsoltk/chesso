package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.Capture
import com.github.zsoltk.rf1.model.move.targetPositions

data class UiState(
    private val gameState: GameState,
    val selectedPosition: Position? = null
) {
    private val lastMovePositions: List<Position> =
        gameState.lastMove?.let { listOf(it.from, it.to) } ?: emptyList()

    private val uiSelectedPositions: List<Position> =
        selectedPosition?.let { listOf(it) } ?: emptyList()

    val highlightedPositions: List<Position> =
        lastMovePositions + uiSelectedPositions

    private val ownPiecePositions: List<Position> =
        gameState.board.pieces
            .filter { (_, piece) -> piece.set == gameState.boardState.toMove }
            .map { it.key }

    val possibleCaptures: List<Position> =
        possibleMoves { it.preMove is Capture }.targetPositions()

    val possibleMovesWithoutCaptures: List<Position> =
        possibleMoves { it.preMove !is Capture }.targetPositions()

    fun possibleMoves(predicate: (BoardMove) -> Boolean = { true }) =
        selectedPosition?.let {
            gameState.legalMovesFrom(it)
                .filter(predicate)
        } ?: emptyList()

    val clickablePositions: List<Position> =
        ownPiecePositions +
            possibleCaptures +
            possibleMovesWithoutCaptures

    fun deselect(): UiState = copy(
        selectedPosition = null
    )

    fun select(position: Position): UiState =
        if (selectedPosition == position) {
            deselect()
        } else {
            copy(
                selectedPosition = position
            )
        }
}
