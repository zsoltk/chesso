package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.state.GameSnaphotState
import com.github.zsoltk.rf1.model.move.BoardMove

interface Piece {

    val set: Set

    val symbol: String

    val textSymbol: String

    val value: Int

    /**
     * List of moves that are legally possible for the piece without applying pin / check constraints
     */
    fun pseudoLegalMoves(gameSnaphotState: GameSnaphotState, checkCheck: Boolean): List<BoardMove> = emptyList()
}
