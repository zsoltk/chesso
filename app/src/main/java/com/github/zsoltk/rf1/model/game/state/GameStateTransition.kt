package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.move.AppliedMove

sealed class GameStateDescriptor

data class InitialState(
    val initialSnaphotState: GameSnaphotState,
) : GameStateDescriptor()

data class GameStateTransition(
    val toSnaphotState: GameSnaphotState,
    val fromSnaphotState: GameSnaphotState,
    val move: AppliedMove
) : GameStateDescriptor()
