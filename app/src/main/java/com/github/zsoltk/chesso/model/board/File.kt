package com.github.zsoltk.chesso.model.board

enum class File {
    a, b, c, d, e, f, g, h
}

operator fun File.get(rank: Int): Position =
    Position.values()[this.ordinal * 8 + (rank - 1)]


operator fun File.get(rank: Rank): Position =
    Position.values()[this.ordinal * 8 + rank.ordinal]
