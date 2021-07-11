package com.github.zsoltk.chesso.ui.renderer.square

import com.github.zsoltk.chesso.ui.renderer.square.decoration.DatasetVisualiser
import com.github.zsoltk.chesso.ui.renderer.square.decoration.DefaultHighlightSquare
import com.github.zsoltk.chesso.ui.renderer.square.decoration.DefaultSquareBackground
import com.github.zsoltk.chesso.ui.renderer.square.decoration.DefaultSquarePositionLabel
import com.github.zsoltk.chesso.ui.renderer.square.decoration.TargetMarks

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
