package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.move.AppliedMove

sealed class GameStateDescriptor

data class InitialState(
    val initialState: GameState,
) : GameStateDescriptor()

data class GameStateTransition(
    val toState: GameState,
    val fromState: GameState,
    val move: AppliedMove
) : GameStateDescriptor()
