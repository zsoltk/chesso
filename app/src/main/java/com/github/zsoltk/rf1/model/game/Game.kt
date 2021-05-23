package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class Game {

    var states by mutableStateOf(listOf(GameState()))

    var currentIndex by mutableStateOf(0)

    val hasPrevIndex: Boolean
        get() = currentIndex > 0

    val hasNextIndex: Boolean
        get() = currentIndex < states.lastIndex

    val currentState: GameState
        get() = states[currentIndex]

    fun moves(): List<Move> =
        states
            .map { gameState -> gameState.move }
            .filterNotNull()
}

