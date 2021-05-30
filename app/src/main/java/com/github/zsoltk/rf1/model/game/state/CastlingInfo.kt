package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Rook
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

data class CastlingInfo(
    val holders: Map<Set, Holder> = mapOf(
        WHITE to Holder(),
        BLACK to Holder()
    )
) {
    data class Holder(
        val kingHasMoved: Boolean = false,
        val kingSideRookHasMoved: Boolean = false,
        val queenSideRookHasMoved: Boolean = false,
    ) {
        val canCastleKingSide: Boolean
            get() = !kingHasMoved && !kingSideRookHasMoved

        val canCastleQueenSide: Boolean
            get() = !kingHasMoved && !queenSideRookHasMoved
    }

    operator fun get(set: Set) = holders[set]!!

    fun apply(boardMove: BoardMove): CastlingInfo {
        val move = boardMove.move
        val piece = boardMove.piece
        val set = piece.set
        val holder = holders[set]!!

        val kingSideRookInitialPosition = if (set == WHITE) h1 else h8
        val queenSideRookInitialPosition = if (set == WHITE) a1 else a8

        val updatedHolder = holder.copy(
            kingHasMoved = holder.kingHasMoved || piece is King,
            kingSideRookHasMoved = holder.kingSideRookHasMoved || piece is Rook && move.from == kingSideRookInitialPosition,
            queenSideRookHasMoved = holder.queenSideRookHasMoved || piece is Rook && move.from == queenSideRookInitialPosition,
        )

        return copy(
            holders = holders
                .minus(set)
                .plus(set to updatedHolder)
        )
    }

    companion object {
        fun from(board: Board): CastlingInfo {
            val whitePieces = board.pieces(WHITE)
            val whiteHolder = Holder(
                kingHasMoved = whitePieces[e1] !is King,
                kingSideRookHasMoved = whitePieces[h1] !is Rook,
                queenSideRookHasMoved = whitePieces[a1] !is Rook,
            )
            val blackPieces = board.pieces(BLACK)
            val blackHolder = Holder(
                kingHasMoved = blackPieces[e8] !is King,
                kingSideRookHasMoved = blackPieces[h8] !is Rook,
                queenSideRookHasMoved = blackPieces[a8] !is Rook,
            )

            return CastlingInfo(
                mapOf(
                    WHITE to whiteHolder,
                    BLACK to blackHolder
                )
            )
        }
    }
}
