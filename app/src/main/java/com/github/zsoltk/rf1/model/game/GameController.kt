package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.game.state.UiState
import com.github.zsoltk.rf1.model.move.targetPositions
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.preset.Preset
import com.github.zsoltk.rf1.model.game.state.GameStateDescriptor
import com.github.zsoltk.rf1.model.game.state.InitialState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.Capture
import com.github.zsoltk.rf1.model.move.Promotion
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Queen
import com.github.zsoltk.rf1.model.piece.Set
import java.lang.IllegalStateException

class GameController(
    private val game: Game,
    private val uiState: UiState,
    private val onPromotion: (() -> Unit)? = null,
    preset: Preset? = null
) {
    init {
        preset?.let { applyPreset(it) }
    }

    private val gameState: GameState
        get() = game.currentState

    private val boardState: BoardState
        get() = gameState.boardState

    val transitionState: GameStateDescriptor
        get() = game.currentTransition

    private var promotionState: PromotionState =
        PromotionState.None

    val toMove: Set
        get() = boardState.toMove

    private sealed class PromotionState {
        object None : PromotionState()
        data class Await(val position: Position) : PromotionState()
        data class ContinueWith(val piece: Piece) : PromotionState()
    }

    fun reset(gameState: GameState = GameState()) {
        game.states = listOf(gameState)
        game.transitions = listOf(InitialState(gameState))
        uiState.selectedPosition = null
    }

    fun applyPreset(preset: Preset) {
        reset()
        preset.apply(this)
    }

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
            possibleCaptures() +
            possibleMovesWithoutCaptures()

    private fun ownPiecePositions(): List<Position> =
        boardState.board.pieces
            .filter { (position, _) -> position.hasOwnPiece() }
            .map { it.key }

    fun possibleCaptures(): List<Position> =
        possibleMoves { it.preMove is Capture }.targetPositions()

    fun possibleMovesWithoutCaptures(): List<Position> =
        possibleMoves { it.preMove !is Capture }.targetPositions()

    private fun possibleMoves(predicate: (BoardMove) -> Boolean = { true }) =
        uiState.selectedPosition?.let {
            gameState.legalMovesFrom(it)
                .filter(predicate)
        } ?: emptyList()

    private fun Position.hasOwnPiece() =
        square(this).hasPiece(boardState.toMove)

    fun onClick(position: Position) {
        if (gameState.resolution != Resolution.IN_PROGRESS) return
        if (position.hasOwnPiece()) {
            selectPosition(position)
        } else if (canMoveTo(position)) {
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
        position in possibleMoves().targetPositions()

    fun applyMove(from: Position, to: Position) {
        val boardMove = findBoardMove(from, to) ?: return
        applyMove(boardMove)
    }

    private fun applyMove(boardMove: BoardMove) {
        var states = game.states.toMutableList()
        var transitions = game.transitions.toMutableList()

        val currentIndex = game.currentIndex
        val transition = gameState.calculateAppliedMove(
            boardMove = boardMove,
            boardStatesSoFar = states.subList(0, currentIndex + 1).map { it.boardState }
        )

        states[currentIndex] = transition.fromState
        states = states.subList(0, currentIndex + 1)
        transitions[currentIndex] = transition
        transitions = transitions.subList(0, currentIndex + 1)

        game.currentIndex = states.lastIndex
        game.states = states + transition.toState
        game.transitions = transitions + transition
        stepForward()
    }

    private fun findBoardMove(from: Position, to: Position): BoardMove? {
        val legalMoves = gameState
            .legalMovesFrom(from)
            .filter { it.to == to }

        return when {
            legalMoves.isEmpty() -> {
                throw IllegalArgumentException("No legal moves exist between $from -> $to")
            }
            legalMoves.size == 1 -> {
                legalMoves.first()
            }
            legalMoves.all { it.consequence is Promotion } -> {
                handlePromotion(to, legalMoves)
            }
            else -> {
                throw IllegalStateException("Legal moves: $legalMoves")
            }
        }
    }

    private fun handlePromotion(to: Position, legalMoves: List<BoardMove>): BoardMove? {
        if (onPromotion == null && promotionState == PromotionState.None) {
            promotionState = PromotionState.ContinueWith(Queen(gameState.toMove))
        }

        when (val promotion = promotionState) {
            is PromotionState.None -> {
                promotionState = PromotionState.Await(to)
                onPromotion!!.invoke()
            }
            is PromotionState.Await -> {
                throw IllegalStateException()
            }
            is PromotionState.ContinueWith -> {
                promotionState = PromotionState.None
                return legalMoves.find { move ->
                    (move.consequence as Promotion).let {
                        it.piece::class == promotion.piece::class
                    }
                }
            }
        }

        return null
    }

    fun onPromotionPieceSelected(piece: Piece) {
        val state = promotionState
        if (state !is PromotionState.Await) error("Not in expected state: $state")
        val position = state.position
        promotionState = PromotionState.ContinueWith(piece)
        onClick(position)
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

