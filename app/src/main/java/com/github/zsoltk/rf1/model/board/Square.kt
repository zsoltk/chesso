package com.github.zsoltk.rf1.model.board

import androidx.compose.runtime.mutableStateOf
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

sealed class UnboundSquare

/**
 * Any square that cannot be on the board due to its coordinates
 */
object Invalid : UnboundSquare()

/**
 * Any square with valid coordinates
 *
 * [1,1] -> [8,8]
 */
data class Square(
    val file: Int,
    val rank: Int
) : UnboundSquare() {

    constructor(
        file: File,
        rank: Rank
    ) : this(
        file = file.ordinal + 1,
        rank = rank.ordinal + 1
    )

    constructor(
        position: Position
    ) : this(
        file = position.ordinal / 8 + 1,
        rank = position.ordinal % 8 + 1
    )

    private val idx = idx(file, rank)

    val position: Position =
        Position.values()[idx]

    val isDark: Boolean =
        (idx + file % 2) % 2 == 1

    operator fun plus(delta: Delta): UnboundSquare {
        try {
            validate(file + delta.x, rank + delta.y)
        } catch (e: IllegalArgumentException) {
            return Invalid
        }

        return Square(file + delta.x, rank + delta.y)
    }

    private var _piece = mutableStateOf<Piece?>(null)
    var piece: Piece?
        get() = _piece.value
        set(value) { _piece.value = value }

    val isEmpty: Boolean
        get() = piece == null

    val isNotEmpty: Boolean
        get() = !isEmpty

    val hasWhitePiece: Boolean
        get() = piece?.set == WHITE

    val hasBlackPiece: Boolean
        get() = piece?.set == BLACK

    override fun toString(): String =
        File.values()[file - 1].toString() + rank.toString()
}
