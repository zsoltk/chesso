package com.github.zsoltk.rf1.model.board

import com.github.zsoltk.rf1.model.notation.AlgebraicNotation

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
        algebraicNotation: AlgebraicNotation
    ) : this(
        file = algebraicNotation.ordinal / 8 + 1,
        rank = algebraicNotation.ordinal % 8 + 1
    )

    val position: AlgebraicNotation
        get() = AlgebraicNotation.values()[(file - 1) * 8 + (rank - 1)]

    operator fun plus(delta: Delta): UnboundSquare {
        if (file + delta.x < 1 || file + delta.x > 8 || rank + delta.y < 1 || rank + delta.y > 8) return Invalid
        else return Square(file + delta.x, rank + delta.y)
    }

    override fun toString(): String =
        File.values()[file - 1].toString() + rank.toString()
}
