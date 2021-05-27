package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.move.Capture
import com.github.zsoltk.rf1.model.move.Move
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.MoveIntention

fun Piece.singleCaptureMove(
    gameState: GameState,
    deltaFile: Int,
    deltaRank: Int
): BoardMove? {
    val board = gameState.board
    val square = board.find(this) ?: return null
    val target = board[square.file + deltaFile, square.rank + deltaRank] ?: return null

    return when {
        target.hasPiece(set) -> null
        else -> BoardMove(
            move = Move(
                piece = this,
                intent = MoveIntention(from = square.position, to = target.position)
            ),
            preMove = when {
                target.isNotEmpty -> Capture(target.piece!!, target.position)
                else -> null
            }
        )
    }
}

fun Piece.lineMoves(
    gameState: GameState,
    directions: List<Pair<Int, Int>>,
) : List<BoardMove> {
    val moves = mutableListOf<BoardMove>()
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
): List<BoardMove> {
    requireNotNull(square.piece)
    val set = square.piece.set
    val moves = mutableListOf<BoardMove>()

    var i = 0
    while (true) {
        i++
        val target = board[square.file + deltaFile * i, square.rank + deltaRank * i] ?: break
        if (target.hasPiece(set)) {
            break
        }

        val move = Move(piece = square.piece, from = square.position, to = target.position)
        if (target.isEmpty) {
            moves += BoardMove(move)
            continue
        }
        if (target.hasPiece(set.opposite())) {
            moves += BoardMove(
                move = move,
                preMove = Capture(target.piece!!, target.position)
            )
            break
        }
    }

    return moves
}
