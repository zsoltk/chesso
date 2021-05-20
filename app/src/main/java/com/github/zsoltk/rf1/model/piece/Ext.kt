package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.GameState
import com.github.zsoltk.rf1.model.notation.Position

fun Piece.lineMoves(
    gameState: GameState,
    directions: List<Pair<Int, Int>>,
) : List<Position> {
    val moves = mutableListOf<Position>()
    val board = gameState.board
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
): List<Position> {
    requireNotNull(square.piece)
    val set = square.piece.set
    val moves = mutableListOf<Position>()

    var i = 0
    while (true) {
        i++
        val target = board[square.file + deltaFile * i, square.rank + deltaRank * i] ?: break
        if (target.isEmpty) {
            moves += target.position
            continue
        }
        if (target.hasPiece(set.opposite())) {
            moves += target.position
            break
        }
        if (target.hasPiece(set)) {
            break
        }
    }

    return moves
}