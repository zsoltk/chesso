package com.github.zsoltk.chesso.model.game.controller

import com.github.zsoltk.chesso.model.board.Square
import com.github.zsoltk.chesso.model.game.state.BoardState
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.move.targetPositions
import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.dataviz.DatasetVisualisation
import com.github.zsoltk.chesso.model.game.Resolution
import com.github.zsoltk.chesso.model.game.controller.Reducer.Action
import com.github.zsoltk.chesso.model.game.preset.Preset
import com.github.zsoltk.chesso.model.game.state.GameMetaInfo
import com.github.zsoltk.chesso.model.game.state.GamePlayState
import com.github.zsoltk.chesso.model.game.state.PromotionState
import com.github.zsoltk.chesso.model.move.BoardMove
import com.github.zsoltk.chesso.model.move.Promotion
import com.github.zsoltk.chesso.model.piece.Piece
import com.github.zsoltk.chesso.model.piece.Queen
import com.github.zsoltk.chesso.model.piece.Set
import java.lang.IllegalStateException

class GameController(
    val getGamePlayState: () -> GamePlayState,
    private val setGamePlayState: ((GamePlayState) -> Unit)? = null,
    preset: Preset? = null
) {
    init {
        preset?.let { applyPreset(it) }
    }

    private val gamePlayState: GamePlayState
        get() = getGamePlayState()

    private val gameSnapshotState: GameSnapshotState
        get() = gamePlayState.gameState.currentSnapshotState

    private val boardState: BoardState
        get() = gameSnapshotState.boardState

    val toMove: Set
        get() = boardState.toMove

    fun reset(
        gameSnapshotState: GameSnapshotState = GameSnapshotState(),
        gameMetaInfo: GameMetaInfo = GameMetaInfo.createWithDefaults()
    ) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.ResetTo(gameSnapshotState, gameMetaInfo))
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
        if (gameSnapshotState.resolution != Resolution.IN_PROGRESS) return
        if (position.hasOwnPiece()) {
            toggleSelectPosition(position)
        } else if (canMoveTo(position)) {
            val selectedPosition = gamePlayState.uiState.selectedPosition
            requireNotNull(selectedPosition)
            applyMove(selectedPosition, position)
        }
    }

    private fun toggleSelectPosition(position: Position) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.ToggleSelectPosition(position))
        )
    }

    private fun canMoveTo(position: Position) =
        position in gamePlayState.uiState.possibleMoves().targetPositions()

    fun applyMove(from: Position, to: Position) {
        val boardMove = findBoardMove(from, to) ?: return
        applyMove(boardMove)
    }

    fun applyMove(boardMove: BoardMove) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.ApplyMove(boardMove))
        )
    }

    private fun findBoardMove(from: Position, to: Position): BoardMove? {
        val legalMoves = gameSnapshotState
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

    private fun handlePromotion(at: Position, legalMoves: List<BoardMove>): BoardMove? {
        var promotionState = gamePlayState.promotionState
        if (setGamePlayState == null && promotionState == PromotionState.None) {
            promotionState = PromotionState.ContinueWith(Queen(gameSnapshotState.toMove))
        }

        when (val promotion = promotionState) {
            is PromotionState.None -> {
                setGamePlayState?.invoke(
                    Reducer(gamePlayState, Action.RequestPromotion(at))
                )
            }
            is PromotionState.Await -> {
                throw IllegalStateException()
            }
            is PromotionState.ContinueWith -> {
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
            Reducer(gamePlayState, Action.PromoteTo(piece))
        )
        onClick(position)
    }

    fun setVisualisation(visualisation: DatasetVisualisation) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.SetVisualisation(visualisation))
        )
    }

    fun stepForward() {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.StepForward)
        )
    }

    fun stepBackward() {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.StepBackward)
        )
    }

    fun goToMove(index: Int) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.GoToMove(index))
        )
    }
}

