package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.move.AppliedMove
import com.github.zsoltk.rf1.model.piece.Set

data class GameState(
    val states: List<GameSnaphotState> = listOf(GameSnaphotState()),
    val currentIndex: Int = 0
) {
    val hasPrevIndex: Boolean
        get() = currentIndex > 0

    val hasNextIndex: Boolean
        get() = currentIndex < states.lastIndex

    val currentSnaphotState: GameSnaphotState
        get() = states[currentIndex]

    val prevSnaphotState: GameSnaphotState?
        get() = if (hasPrevIndex) states[currentIndex-1] else null

    val toMove: Set
        get() = currentSnaphotState.boardState.toMove

    val resolution: Resolution
        get() = currentSnaphotState.resolution

    fun moves(): List<AppliedMove> =
        states
            .map { gameState -> gameState.move }
            .filterNotNull()
}

