package com.github.zsoltk.rf1.model.board

import com.github.zsoltk.rf1.model.notation.AlgebraicNotation

enum class File {
    a, b, c, d, e, f, g, h
}

operator fun File.get(rank: Int): AlgebraicNotation =
    AlgebraicNotation.values()[this.ordinal * 8 + (rank - 1)]


operator fun File.get(rank: Rank): AlgebraicNotation =
    AlgebraicNotation.values()[this.ordinal * 8 + rank.ordinal]
