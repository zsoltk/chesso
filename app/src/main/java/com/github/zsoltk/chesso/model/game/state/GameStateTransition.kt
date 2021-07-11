package com.github.zsoltk.chesso.model.game.state

import com.github.zsoltk.chesso.model.move.AppliedMove

data class GameStateTransition(
    val fromSnapshotState: GameSnapshotState,
    val toSnapshotState: GameSnapshotState,
    val move: AppliedMove
)
