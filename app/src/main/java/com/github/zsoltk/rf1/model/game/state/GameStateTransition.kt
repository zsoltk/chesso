package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.move.AppliedMove

data class GameStateTransition(
    val move: AppliedMove,
    val updatedCurrentState: GameState,
    val newState: GameState
)
