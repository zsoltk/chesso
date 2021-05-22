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

    fun moves(): List<String> {
        val moves = mutableListOf<String>()
        val move = StringBuilder()

        states.value.forEachIndexed { index, gameState ->
            gameState.move?.let {
                if (index % 2 == 0) move.append("${(index / 2 + 1)}. ")
                move.append(it)
                if (index % 2 == 0) move.append(" ")
            }

            if (index % 2 == 1) {
                moves += move.toString()
                move.clear()
            }
        }

        return moves
    }
}

