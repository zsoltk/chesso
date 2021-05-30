package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.move.AppliedMove

data class GameStateTransition(
    val toState: GameState,
    val fromState: GameState? = null,
    val move: AppliedMove? = null
)
