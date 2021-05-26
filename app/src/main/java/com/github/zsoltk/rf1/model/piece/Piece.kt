package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.move.BoardMove

interface Piece {

    val set: Set

    val symbol: String

    val value: Int

    /**
     * List of moves that are legally possible for the piece without applying pin / check constraints
     */
    fun pseudoLegalMoves(gameState: GameState): List<BoardMove> = emptyList()
}
