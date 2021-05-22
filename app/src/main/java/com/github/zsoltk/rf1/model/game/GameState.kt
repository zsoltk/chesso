package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.piece.Set

data class GameState(
    val toMove: Set = Set.WHITE,
    val move: Move? = null,
    val lastMove: Move? = null,
    val board: Board = Board()
)
