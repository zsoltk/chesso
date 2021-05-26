package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.move.AppliedMove
import com.github.zsoltk.rf1.model.piece.Set

class Game {

    var states by mutableStateOf(listOf(GameState()))

    var currentIndex by mutableStateOf(0)

    val hasPrevIndex: Boolean
        get() = currentIndex > 0

    val hasNextIndex: Boolean
        get() = currentIndex < states.lastIndex

    val currentState: GameState
        get() = states[currentIndex]

    val toMove: Set
        get() = currentState.boardState.toMove

    val resolution: Resolution
        get() = currentState.resolution

    fun moves(): List<AppliedMove> =
        states
            .map { gameState -> gameState.move }
            .filterNotNull()
}

