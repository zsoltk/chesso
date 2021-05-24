package com.github.zsoltk.rf1.model.game

data class CalculatedMove(
    val move: Move,
    val updatedCurrentState: GameState,
    val newState: GameState
)
