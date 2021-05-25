package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.BoardState
import com.github.zsoltk.rf1.model.game.Move

fun Piece.singleCaptureMove(
    boardState: BoardState,
    deltaFile: Int,
    deltaRank: Int
): Move? {
    val board = boardState.board
    val square = board.find(this) ?: return null
    val target = board[square.file + deltaFile, square.rank + deltaRank] ?: return null

    return if (target.isEmpty || target.hasPiece(set.opposite())) Move(
        from = square.position,
        to = target.position,
        piece = this
    ) else null
}

fun Piece.lineMoves(
    boardState: BoardState,
    directions: List<Pair<Int, Int>>,
) : List<Move> {
    val moves = mutableListOf<Move>()
    val board = boardState.board
    val square = board.find(this) ?: return emptyList()

    directions.map {
        moves += lineMoves(board, square, it.first, it.second)
    }

    return moves
}

fun lineMoves(
    board: Board,
    square: Square,
    deltaFile: Int,
    deltaRank: Int
): List<Move> {
    requireNotNull(square.piece)
    val set = square.piece.set
    val moves = mutableListOf<Move>()

    var i = 0
    while (true) {
        i++
        val target = board[square.file + deltaFile * i, square.rank + deltaRank * i] ?: break
        if (target.isEmpty) {
            moves += Move(from = square.position, to = target.position, piece = square.piece)
            continue
        }
        if (target.hasPiece(set.opposite())) {
            moves += Move(from = square.position, to = target.position, piece = square.piece)
            break
        }
        if (target.hasPiece(set)) {
            break
        }
    }

    return moves
}
