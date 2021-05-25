package com.github.zsoltk.rf1.model.game

data class AppliedMove(
    val move: Move,
    val updatedCurrentState: GameState,
    val newState: GameState
)
