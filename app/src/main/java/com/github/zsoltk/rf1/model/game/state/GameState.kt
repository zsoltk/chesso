package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.move.AppliedMove
import com.github.zsoltk.rf1.model.piece.Set

data class GameState(
    val states: List<GameSnapshotState> = listOf(GameSnapshotState()),
    val currentIndex: Int = 0
) {
    val hasPrevIndex: Boolean
        get() = currentIndex > 0

    val hasNextIndex: Boolean
        get() = currentIndex < states.lastIndex

    val currentSnapshotState: GameSnapshotState
        get() = states[currentIndex]

    val prevSnapshotState: GameSnapshotState?
        get() = if (hasPrevIndex) states[currentIndex-1] else null

    val toMove: Set
        get() = currentSnapshotState.boardState.toMove

    val resolution: Resolution
        get() = currentSnapshotState.resolution

    fun moves(): List<AppliedMove> =
        states
            .map { gameState -> gameState.move }
            .filterNotNull()
}

