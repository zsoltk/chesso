package com.github.zsoltk.chesso.ui.chess.board

import com.github.zsoltk.chesso.ui.chess.board.decoration.DecorateSquares
import com.github.zsoltk.chesso.ui.chess.board.decoration.Pieces
import com.github.zsoltk.chesso.ui.chess.square.DefaultSquareRenderer

object DefaultBoardRenderer : BoardRenderer {

    override val decorations: List<BoardDecoration> =
        listOf(
            DecorateSquares(DefaultSquareRenderer.decorations),
            Pieces
        )
}
