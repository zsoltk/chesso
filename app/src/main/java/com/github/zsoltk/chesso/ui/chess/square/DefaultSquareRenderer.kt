package com.github.zsoltk.chesso.ui.chess.square

import com.github.zsoltk.chesso.ui.chess.square.decoration.DatasetVisualiser
import com.github.zsoltk.chesso.ui.chess.square.decoration.DefaultHighlightSquare
import com.github.zsoltk.chesso.ui.chess.square.decoration.DefaultSquareBackground
import com.github.zsoltk.chesso.ui.chess.square.decoration.DefaultSquarePositionLabel
import com.github.zsoltk.chesso.ui.chess.square.decoration.TargetMarks

object DefaultSquareRenderer : SquareRenderer {

    override val decorations: List<SquareDecoration> =
        listOf(
            DefaultSquareBackground,
            DefaultHighlightSquare,
            DefaultSquarePositionLabel,
            DatasetVisualiser,
            TargetMarks
        )
}
