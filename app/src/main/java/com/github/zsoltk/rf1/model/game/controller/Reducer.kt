package com.github.zsoltk.rf1.model.game.controller

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.dataviz.DatasetVisualisation
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.game.state.PromotionState
import com.github.zsoltk.rf1.model.game.state.UiState
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
        data class SetVisualisation(val visualisation: DatasetVisualisation) : Action()
    }

    operator fun invoke(gamePlayState: GamePlayState, action: Action): GamePlayState {
        val gameState = gamePlayState.gameState
        val currentSnapshotState = gameState.currentSnapshotState

        return when (action) {
            is Action.StepForward -> gamePlayState.stepBy(1)
            is Action.StepBackward -> gamePlayState.stepBy(-1)
            is Action.GoToMove -> gamePlayState.goToSnapshot(action.moveIndex + 1)
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
                var states = gameState.states.toMutableList()
                val currentIndex = gameState.currentIndex
                val transition = currentSnapshotState.calculateAppliedMove(
                    boardMove = action.boardMove,
                    boardStatesSoFar = states.subList(0, currentIndex + 1).map { it.boardState }
                )

                states[currentIndex] = transition.fromSnapshotState
                states = states.subList(0, currentIndex + 1)
                states.add(transition.toSnapshotState)

                GamePlayState(
                    gameState = gameState.copy(
                        states = states,
                        currentIndex = states.lastIndex,
                        lastActiveState = currentSnapshotState
                    ),
                    visualisation = gamePlayState.visualisation
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
            is Action.SetVisualisation -> {
                gamePlayState.copy(
                    visualisation = action.visualisation
                )
            }
        }
    }

    private fun GamePlayState.stepBy(step: Int): GamePlayState {
        val newIndex = gameState.currentIndex + step
        if (newIndex !in 0..gameState.states.lastIndex) return this
        return goToSnapshot(newIndex)
    }

    private fun GamePlayState.goToSnapshot(index: Int): GamePlayState {
        if (index !in 0..gameState.states.lastIndex) return this

        return copy(
            gameState = gameState.copy(
                currentIndex = index,
                lastActiveState = gameState.currentSnapshotState
            ),
            uiState = UiState(gameState.states[index])
        )
    }
}
