package com.github.zsoltk.rf1.ui.renderer.board

import com.github.zsoltk.rf1.ui.decoration.board.BoardDecoration
import com.github.zsoltk.rf1.ui.decoration.board.DecorateSquares
import com.github.zsoltk.rf1.ui.decoration.board.Pieces
import com.github.zsoltk.rf1.ui.renderer.square.DefaultSquareRenderer

object DefaultBoardRenderer : BoardRenderer {

    override val decorations: List<BoardDecoration> =
        listOf(
            DecorateSquares(DefaultSquareRenderer.decorations),
            Pieces
        )
}
