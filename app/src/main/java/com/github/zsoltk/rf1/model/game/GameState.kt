package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.piece.Set

data class GameState(
    val nextMove: Set = Set.WHITE,
    val board: Board = Board()
)
