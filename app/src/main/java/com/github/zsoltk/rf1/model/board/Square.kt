package com.github.zsoltk.rf1.model.board

import com.github.zsoltk.rf1.model.notation.AlgebraicNotation


/**
 * [1,1] -> [8,8]
 */
data class Square(
    val file: Int,
    val rank: Int
) {
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

    override fun toString(): String =
        File.values()[file - 1].toString() + rank.toString()
}
