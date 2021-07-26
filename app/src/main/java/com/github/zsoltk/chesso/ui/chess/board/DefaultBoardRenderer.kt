package com.github.zsoltk.chesso.ui.chess.board

import com.github.zsoltk.chesso.ui.chess.board.decoration.DecoratedSquares
import com.github.zsoltk.chesso.ui.chess.pieces.Pieces
import com.github.zsoltk.chesso.ui.chess.square.DefaultSquareRenderer

object DefaultBoardRenderer : BoardRenderer {

    override val decorations: List<BoardDecoration> =
        listOf(
            DecoratedSquares(DefaultSquareRenderer.decorations),
            Pieces
        )
}
