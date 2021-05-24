package com.github.zsoltk.rf1.model.board

import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.notation.Position.a1
import com.github.zsoltk.rf1.model.notation.Position.a2
import com.github.zsoltk.rf1.model.notation.Position.a7
import com.github.zsoltk.rf1.model.notation.Position.a8
import com.github.zsoltk.rf1.model.notation.Position.b1
import com.github.zsoltk.rf1.model.notation.Position.b2
import com.github.zsoltk.rf1.model.notation.Position.b7
import com.github.zsoltk.rf1.model.notation.Position.b8
import com.github.zsoltk.rf1.model.notation.Position.c1
import com.github.zsoltk.rf1.model.notation.Position.c2
import com.github.zsoltk.rf1.model.notation.Position.c7
import com.github.zsoltk.rf1.model.notation.Position.c8
import com.github.zsoltk.rf1.model.notation.Position.d1
import com.github.zsoltk.rf1.model.notation.Position.d2
import com.github.zsoltk.rf1.model.notation.Position.d7
import com.github.zsoltk.rf1.model.notation.Position.d8
import com.github.zsoltk.rf1.model.notation.Position.e1
import com.github.zsoltk.rf1.model.notation.Position.e2
import com.github.zsoltk.rf1.model.notation.Position.e7
import com.github.zsoltk.rf1.model.notation.Position.e8
import com.github.zsoltk.rf1.model.notation.Position.f1
import com.github.zsoltk.rf1.model.notation.Position.f2
import com.github.zsoltk.rf1.model.notation.Position.f7
import com.github.zsoltk.rf1.model.notation.Position.f8
import com.github.zsoltk.rf1.model.notation.Position.g1
import com.github.zsoltk.rf1.model.notation.Position.g2
import com.github.zsoltk.rf1.model.notation.Position.g7
import com.github.zsoltk.rf1.model.notation.Position.g8
import com.github.zsoltk.rf1.model.notation.Position.h1
import com.github.zsoltk.rf1.model.notation.Position.h2
import com.github.zsoltk.rf1.model.notation.Position.h7
import com.github.zsoltk.rf1.model.notation.Position.h8
import com.github.zsoltk.rf1.model.piece.Bishop
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Knight
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Queen
import com.github.zsoltk.rf1.model.piece.Rook
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE
import java.lang.IllegalArgumentException

data class Board(
    val pieces: Map<Position, Piece>
) {
    constructor() : this(
        pieces = initialPieces
    )

    private val squares = Position.values().map { position ->
        position to Square(position, pieces[position])
    }.toMap()

    operator fun get(position: Position): Square =
        squares[position]!!

    operator fun get(file: Int, rank: Int): Square? {
        return try {
            val position = Position.from(file, rank)
            squares[position]
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun find(piece: Piece): Square? =
        squares.values.firstOrNull { it.piece == piece }

    fun pieces(set: Set): Map<Position, Piece> =
        pieces.filter { (_, piece) -> piece.set == set }
}

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
