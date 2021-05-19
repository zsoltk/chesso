package com.github.zsoltk.rf1.model.board

fun idx(file: Int, rank: Int): Int =
    (file - 1) * 8 + (rank - 1)

fun validate(file: Int, rank: Int) {
    require(file >= 1)
    require(file <= 8)
    require(rank >= 1)
    require(rank <= 8)
}

