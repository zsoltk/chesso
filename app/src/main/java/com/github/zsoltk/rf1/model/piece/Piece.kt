package com.github.zsoltk.rf1.model.piece

import android.os.Parcelable
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.move.BoardMove

interface Piece : Parcelable {

    val set: Set

    val symbol: String

    val textSymbol: String

    val value: Int

    /**
     * List of moves that are legally possible for the piece without applying pin / check constraints
     */
    fun pseudoLegalMoves(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> =
        emptyList()

    /**
     * Not required for the game, adds supplementary info only. 
     */
    fun pressure(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> =
        pseudoLegalMoves(gameSnapshotState, checkCheck)
}
