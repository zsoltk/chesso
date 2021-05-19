package com.github.zsoltk.rf1.model.board

import androidx.compose.runtime.mutableStateMapOf
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.a1
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.a2
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.a7
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.a8
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.b1
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.b2
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.b7
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.b8
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.c1
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.c2
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.c7
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.c8
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.d1
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.d2
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.d7
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.d8
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.e1
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.e2
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.e7
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.e8
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.f1
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.f2
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.f7
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.f8
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.g1
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.g2
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.g7
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.g8
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.h1
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.h2
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.h7
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.h8
import com.github.zsoltk.rf1.model.piece.Bishop
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Knight
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Queen
import com.github.zsoltk.rf1.model.piece.Rook
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class Board {

    private val squares = mutableStateMapOf<AlgebraicNotation, Square>()

    private val initialPieces = mapOf(
        a8 to Rook(BLACK),
        b8 to Knight(BLACK),
        c8 to Bishop(BLACK),
        d8 to Queen(BLACK),
        e8 to King(BLACK),
        f8 to Bishop(BLACK),
        g8 to Knight(BLACK),
        h8 to Rook(BLACK),

        a7 to Pawn(BLACK),
        b7 to Pawn(BLACK),
        c7 to Pawn(BLACK),
        d7 to Pawn(BLACK),
        e7 to Pawn(BLACK),
        f7 to Pawn(BLACK),
        g7 to Pawn(BLACK),
        h7 to Pawn(BLACK),

        a2 to Pawn(WHITE),
        b2 to Pawn(WHITE),
        c2 to Pawn(WHITE),
        d2 to Pawn(WHITE),
        e2 to Pawn(WHITE),
        f2 to Pawn(WHITE),
        g2 to Pawn(WHITE),
        h2 to Pawn(WHITE),

        a1 to Rook(WHITE),
        b1 to Knight(WHITE),
        c1 to Bishop(WHITE),
        d1 to Queen(WHITE),
        e1 to King(WHITE),
        f1 to Bishop(WHITE),
        g1 to Knight(WHITE),
        h1 to Rook(WHITE),
    )

    init {
        AlgebraicNotation.values().forEach { an ->
            squares[an] = Square(an)
        }

        initialPieces.forEach { (an, piece) ->
            squares[an]!!.piece = piece
        }
    }

    operator fun get(file: Int, rank: Int): Square {
        val an = AlgebraicNotation.from(file, rank)
        return squares[an]!!
    }


}
