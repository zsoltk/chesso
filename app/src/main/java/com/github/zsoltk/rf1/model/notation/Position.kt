package com.github.zsoltk.rf1.model.notation

import com.github.zsoltk.rf1.model.board.idx
import com.github.zsoltk.rf1.model.board.validate

enum class Position {
    a1, a2, a3, a4, a5, a6, a7, a8,
    b1, b2, b3, b4, b5, b6, b7, b8,
    c1, c2, c3, c4, c5, c6, c7, c8,
    d1, d2, d3, d4, d5, d6, d7, d8,
    e1, e2, e3, e4, e5, e6, e7, e8,
    f1, f2, f3, f4, f5, f6, f7, f8,
    g1, g2, g3, g4, g5, g6, g7, g8,
    h1, h2, h3, h4, h5, h6, h7, h8;

    val file: Int = ordinal / 8 + 1

    val rank: Int = ordinal % 8 + 1

    companion object {

        fun from(file: Int, rank: Int): Position {
            validate(file, rank)

            val idx = idx(file, rank)

            return values()[idx]
        }
    }
}
