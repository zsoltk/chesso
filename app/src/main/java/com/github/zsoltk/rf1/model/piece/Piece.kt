package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.move.BoardMove

interface Piece {

    val set: Set

    val symbol: String

    val value: Int

    fun moves(boardState: BoardState): List<BoardMove> = emptyList()
}
