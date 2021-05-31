package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.game.state.GameSnaphotState
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
    val gameState: GameState,
    private val onPromotion: (() -> Unit)? = null,
    preset: Preset? = null
) {
    init {
        preset?.let { applyPreset(it) }
    }

    val gameSnaphotState: GameSnaphotState
        get() = gameState.currentSnaphotState

    var uiState by mutableStateOf(UiState(gameSnaphotState))

    private val boardState: BoardState
        get() = gameSnaphotState.boardState

    private var promotionState: PromotionState =
        PromotionState.None

    val toMove: Set
        get() = boardState.toMove

    private sealed class PromotionState {
        object None : PromotionState()
        data class Await(val position: Position) : PromotionState()
        data class ContinueWith(val piece: Piece) : PromotionState()
    }

    fun reset(gameSnaphotState: GameSnaphotState = GameSnaphotState()) {
        gameState.states = listOf(gameSnaphotState)
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
        if (gameSnaphotState.resolution != Resolution.IN_PROGRESS) return
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
        var states = gameState.states.toMutableList()
        val currentIndex = gameState.currentIndex
        val transition = gameSnaphotState.calculateAppliedMove(
            boardMove = boardMove,
            boardStatesSoFar = states.subList(0, currentIndex + 1).map { it.boardState }
        )

        states[currentIndex] = transition.fromSnaphotState
        states = states.subList(0, currentIndex + 1)
        gameState.currentIndex = states.lastIndex
        gameState.states = states + transition.toSnaphotState
        stepForward()
    }

    private fun findBoardMove(from: Position, to: Position): BoardMove? {
        val legalMoves = gameSnaphotState
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
            promotionState = PromotionState.ContinueWith(Queen(gameSnaphotState.toMove))
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
        gameState.hasPrevIndex

    fun canStepForward(): Boolean =
        gameState.hasNextIndex

    fun stepForward() {
        if (canStepForward()) {
            gameState.currentIndex++
            uiState = UiState(gameSnaphotState)
        }
    }

    fun stepBackward() {
        if (canStepBack()) {
            gameState.currentIndex--
            uiState = UiState(gameSnaphotState)
        }
    }
}

