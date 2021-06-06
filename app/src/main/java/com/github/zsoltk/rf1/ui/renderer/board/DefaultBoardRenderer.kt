package com.github.zsoltk.rf1.ui.renderer.board

import com.github.zsoltk.rf1.ui.renderer.board.decoration.DecorateSquares
import com.github.zsoltk.rf1.ui.renderer.board.decoration.Pieces
import com.github.zsoltk.rf1.ui.renderer.square.DefaultSquareRenderer

object DefaultBoardRenderer : BoardRenderer {

    override val decorations: List<BoardDecoration> =
        listOf(
            DecorateSquares(DefaultSquareRenderer.decorations),
            Pieces
        )
}
