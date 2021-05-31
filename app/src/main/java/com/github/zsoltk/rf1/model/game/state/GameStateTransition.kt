package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.move.AppliedMove

data class GameStateTransition(
    val fromSnaphotState: GameSnaphotState,
    val toSnaphotState: GameSnaphotState,
    val move: AppliedMove
)
