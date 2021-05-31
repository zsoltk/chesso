package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.game.state.GameSnaphotState
import com.github.zsoltk.rf1.model.move.targetPositions
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.preset.Preset
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.game.state.PromotionState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.Promotion
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Queen
import com.github.zsoltk.rf1.model.piece.Set
import java.lang.IllegalStateException

class GameController(
    val getGamePlayState: () -> GamePlayState,
    private val setGamePlayState: ((GamePlayState) -> Unit)? = null,
    preset: Preset? = null
) {
    init {
        preset?.let { applyPreset(it) }
    }

    val gamePlayState: GamePlayState
        get() = getGamePlayState()

    val gameSnaphotState: GameSnaphotState
        get() = gamePlayState.gameState.currentSnaphotState

    private val boardState: BoardState
        get() = gameSnaphotState.boardState

    val toMove: Set
        get() = boardState.toMove

    fun reset(gameSnaphotState: GameSnaphotState = GameSnaphotState()) {
        setGamePlayState?.invoke(
            GamePlayState(
                gameState = GameState(
                    states = listOf(gameSnaphotState)
                )
            )
        )
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
            val selectedPosition = gamePlayState.uiState.selectedPosition
            requireNotNull(selectedPosition)
            applyMove(selectedPosition, position)
        }
    }

    private fun selectPosition(position: Position) {
        setGamePlayState?.invoke(
            gamePlayState.copy(
                uiState = gamePlayState.uiState.select(position)
            )
        )
    }

    private fun canMoveTo(position: Position) =
        position in gamePlayState.uiState.possibleMoves().targetPositions()

    fun applyMove(from: Position, to: Position) {
        val boardMove = findBoardMove(from, to) ?: return
        applyMove(boardMove)
    }

    private fun applyMove(boardMove: BoardMove) {
        var states = gamePlayState.gameState.states.toMutableList()
        val currentIndex = gamePlayState.gameState.currentIndex
        val transition = gameSnaphotState.calculateAppliedMove(
            boardMove = boardMove,
            boardStatesSoFar = states.subList(0, currentIndex + 1).map { it.boardState }
        )

        states[currentIndex] = transition.fromSnaphotState
        states = states.subList(0, currentIndex + 1)
        states.add(transition.toSnaphotState)

        setGamePlayState?.invoke(
            GamePlayState(
                gameState = gamePlayState.gameState.copy(
                    states = states,
                    currentIndex = states.lastIndex
                )
            )
        )
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
        var promotionState = gamePlayState.promotionState
        if (setGamePlayState == null && promotionState == PromotionState.None) {
            promotionState = PromotionState.ContinueWith(Queen(gameSnaphotState.toMove))
        }

        when (val promotion = promotionState) {
            is PromotionState.None -> {
                setGamePlayState?.invoke(
                    gamePlayState.copy(
                        uiState = gamePlayState.uiState.copy(
                            showPromotionDialog = true
                        ),
                        promotionState = PromotionState.Await(to)
                    )
                )
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
        val state = gamePlayState.promotionState
        if (state !is PromotionState.Await) error("Not in expected state: $state")
        val position = state.position
        setGamePlayState?.invoke(
            gamePlayState.copy(
                uiState = gamePlayState.uiState.copy(
                    showPromotionDialog = false
                ),
                promotionState = PromotionState.ContinueWith(piece)
            )
        )
        onClick(position)
    }

    fun canStepBack(): Boolean =
        gamePlayState.gameState.hasPrevIndex

    fun canStepForward(): Boolean =
        gamePlayState.gameState.hasNextIndex

    fun stepForward() {
        if (canStepForward()) {
            setGamePlayState?.invoke(
                GamePlayState(
                    gameState = gamePlayState.gameState.copy(
                        currentIndex = gamePlayState.gameState.currentIndex + 1
                    )
                )
            )
        }
    }

    fun stepBackward() {
        if (canStepBack()) {
            setGamePlayState?.invoke(
                GamePlayState(
                    gameState = gamePlayState.gameState.copy(
                        currentIndex = gamePlayState.gameState.currentIndex - 1
                    )
                )
            )
        }
    }
}

