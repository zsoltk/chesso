package com.github.zsoltk.rf1.model.game.controller

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.game.state.PromotionState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.piece.Piece

object Reducer {

    sealed class Action {
        object StepForward : Action()
        object StepBackward : Action()
        data class GoToMove(val moveIndex: Int) : Action()
        data class ResetTo(val gameSnapshotState: GameSnapshotState) : Action()
        data class ToggleSelectPosition(val position: Position) : Action()
        data class ApplyMove(val boardMove: BoardMove) : Action()
        data class RequestPromotion(val at: Position) : Action()
        data class PromoteTo(val piece: Piece) : Action()
    }

    operator fun invoke(gamePlayState: GamePlayState, action: Action): GamePlayState =
        when (action) {
            is Action.StepForward -> {
                if (gamePlayState.gameState.hasNextIndex) {
                    gamePlayState.copy(
                        gameState = gamePlayState.gameState.copy(
                            currentIndex = gamePlayState.gameState.currentIndex + 1,
                            lastActiveState = gamePlayState.gameState.currentSnapshotState
                        ),
                        uiState = gamePlayState.uiState.copy(
                            selectedPosition = null
                        )
                    )
                } else gamePlayState
            }
            is Action.StepBackward -> {
                if (gamePlayState.gameState.hasPrevIndex) {
                    gamePlayState.copy(
                        gameState = gamePlayState.gameState.copy(
                            currentIndex = gamePlayState.gameState.currentIndex - 1,
                            lastActiveState = gamePlayState.gameState.currentSnapshotState
                        ),
                        uiState = gamePlayState.uiState.copy(
                            selectedPosition = null
                        )
                    )
                } else gamePlayState
            }
            is Action.GoToMove -> {
                val snapshotIndex = action.moveIndex + 1
                if (snapshotIndex in gamePlayState.gameState.states.indices) {
                    gamePlayState.copy(
                        gameState = gamePlayState.gameState.copy(
                            currentIndex = snapshotIndex,
                            lastActiveState = gamePlayState.gameState.currentSnapshotState
                        ),
                        uiState = gamePlayState.uiState.copy(
                            selectedPosition = null
                        )
                    )
                } else gamePlayState
            }
            is Action.ResetTo -> {
                GamePlayState(
                    gameState = GameState(
                        states = listOf(action.gameSnapshotState)
                    )
                )
            }
            is Action.ToggleSelectPosition -> {
                if (gamePlayState.uiState.selectedPosition == action.position) {
                    gamePlayState.copy(
                        uiState = gamePlayState.uiState.copy(
                            selectedPosition = null
                        )
                    )
                } else {
                    gamePlayState.copy(
                        uiState = gamePlayState.uiState.copy(
                            selectedPosition = action.position
                        )
                    )
                }
            }
            is Action.ApplyMove -> {
                val gameSnapshotState = gamePlayState.gameState.currentSnapshotState
                var states = gamePlayState.gameState.states.toMutableList()
                val currentIndex = gamePlayState.gameState.currentIndex
                val transition = gameSnapshotState.calculateAppliedMove(
                    boardMove = action.boardMove,
                    boardStatesSoFar = states.subList(0, currentIndex + 1).map { it.boardState }
                )

                states[currentIndex] = transition.fromSnapshotState
                states = states.subList(0, currentIndex + 1)
                states.add(transition.toSnapshotState)

                GamePlayState(
                    gameState = gamePlayState.gameState.copy(
                        states = states,
                        currentIndex = states.lastIndex,
                        lastActiveState = gamePlayState.gameState.currentSnapshotState
                    )
                )
            }
            is Action.RequestPromotion -> {
                gamePlayState.copy(
                    uiState = gamePlayState.uiState.copy(
                        showPromotionDialog = true
                    ),
                    promotionState = PromotionState.Await(action.at)
                )
            }
            is Action.PromoteTo -> {
                gamePlayState.copy(
                    uiState = gamePlayState.uiState.copy(
                        showPromotionDialog = false
                    ),
                    promotionState = PromotionState.ContinueWith(action.piece)
                )
            }
        }
}
