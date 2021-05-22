package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.mutableStateOf

class Game {

    val states = mutableStateOf(listOf(GameState()))

    var currentIndex = mutableStateOf(0)

    val hasPrevIndex: Boolean
        get() = currentIndex.value > 0

    val hasNextIndex: Boolean
        get() = currentIndex.value < states.value.lastIndex

    val currentState: GameState
        get() = states.value[currentIndex.value]

    fun moves(): List<Move> =
        states.value
            .map { gameState -> gameState.move }
            .filterNotNull()
}

