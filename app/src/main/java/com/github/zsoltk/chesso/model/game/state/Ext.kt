package com.github.zsoltk.chesso.model.game.state

import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.board.isDarkSquare
import com.github.zsoltk.chesso.model.board.isLightSquare
import com.github.zsoltk.chesso.model.move.BoardMove
import com.github.zsoltk.chesso.model.move.BoardMove.Ambiguity.AMBIGUOUS_FILE
import com.github.zsoltk.chesso.model.move.BoardMove.Ambiguity.AMBIGUOUS_RANK
import com.github.zsoltk.chesso.model.piece.Bishop
import com.github.zsoltk.chesso.model.piece.King
import com.github.zsoltk.chesso.model.piece.Knight
import com.github.zsoltk.chesso.model.piece.Piece
import com.github.zsoltk.chesso.model.piece.Set
import java.util.EnumSet

fun List<BoardState>.hasThreefoldRepetition(): Boolean =
    map { it.hashCode() }
        .groupBy { it }
        .map { it.value.size }
        .any { it > 2 }

fun Map<Position, Piece>.hasInsufficientMaterial(): Boolean =
    when {
        size == 2 && hasWhiteKing() && hasBlackKing() -> true
        size == 3 && hasWhiteKing() && hasBlackKing() && hasBishop() -> true
        size == 3 && hasWhiteKing() && hasBlackKing() && hasKnight() -> true
        size == 4 && hasWhiteKing() && hasBlackKing() && hasBishopsOnSameColor() -> true
        else -> false
    }

private fun Map<Position, Piece>.hasWhiteKing(): Boolean =
    values.find { it.set == Set.WHITE && it is King } != null

private fun Map<Position, Piece>.hasBlackKing(): Boolean =
    values.find { it.set == Set.BLACK && it is King } != null

private fun Map<Position, Piece>.hasBishop(): Boolean =
    values.find { it is Bishop } != null

private fun Map<Position, Piece>.hasKnight(): Boolean =
    values.find { it is Knight } != null

private fun Map<Position, Piece>.hasBishopsOnSameColor(): Boolean {
    val bishops = filter { it.value is Bishop }

    return bishops.size > 1 && (bishops.all { it.key.isLightSquare() } || bishops.all { it.key.isDarkSquare() })
}

fun BoardMove.applyAmbiguity(gameSnapshotState: GameSnapshotState): BoardMove =
    gameSnapshotState.legalMovesTo(to)
        .filter { it.piece.textSymbol == piece.textSymbol }
        .let { similarPieces ->
            val ambiguity = EnumSet.noneOf(BoardMove.Ambiguity::class.java)
            when (similarPieces.size) {
                1 -> this
                else -> {
                    val onSameFile = similarPieces.filter { it.from.file == from.file }
                    if (onSameFile.size == 1) {
                        ambiguity.add(AMBIGUOUS_FILE)
                    } else {
                        val onSameRank = similarPieces.filter { it.from.rank == from.rank }
                        if (onSameRank.size == 1) {
                            ambiguity.add(AMBIGUOUS_RANK)
                        } else {
                            ambiguity.add(AMBIGUOUS_FILE)
                            ambiguity.add(AMBIGUOUS_RANK)
                        }
                    }

                    copy(ambiguity = ambiguity)
                }
            }
        }
