package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.game.state.UiState
import com.github.zsoltk.rf1.model.move.targetPositions
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.preset.Preset
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.Promotion
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Queen
import com.github.zsoltk.rf1.model.piece.Set
import java.lang.IllegalStateException

class GameController(
    val game: Game,
    private val onPromotion: (() -> Unit)? = null,
    preset: Preset? = null
) {
    init {
        preset?.let { applyPreset(it) }
    }

    val gameState: GameState
        get() = game.currentState

    var uiState by mutableStateOf(UiState(gameState))

    private val boardState: BoardState
        get() = gameState.boardState

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
        uiState = uiState.deselect()
    }

    fun applyPreset(preset: Preset) {
        reset()
        preset.apply(this)
    }

    fun square(position: Position): Square =
        boardState.board[position]

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
        uiState = uiState.select(position)
    }

    private fun canMoveTo(position: Position) =
        position in uiState.possibleMoves().targetPositions()

    fun applyMove(from: Position, to: Position) {
        val boardMove = findBoardMove(from, to) ?: return
        applyMove(boardMove)
    }

    private fun applyMove(boardMove: BoardMove) {
        var states = game.states.toMutableList()
        val currentIndex = game.currentIndex
        val transition = gameState.calculateAppliedMove(
            boardMove = boardMove,
            boardStatesSoFar = states.subList(0, currentIndex + 1).map { it.boardState }
        )

        states[currentIndex] = transition.fromState
        states = states.subList(0, currentIndex + 1)
        game.currentIndex = states.lastIndex
        game.states = states + transition.toState
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
            uiState = UiState(gameState)
        }
    }

    fun stepBackward() {
        if (canStepBack()) {
            game.currentIndex--
            uiState = UiState(gameState)
        }
    }
}

