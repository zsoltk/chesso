package com.github.zsoltk.rf1.ui.renderer.square

import com.github.zsoltk.rf1.ui.decoration.square.DefaultHighlightSquare
import com.github.zsoltk.rf1.ui.decoration.square.DefaultSquareBackground
import com.github.zsoltk.rf1.ui.decoration.square.DefaultSquarePositionLabel
import com.github.zsoltk.rf1.ui.decoration.square.SquareDecoration
import com.github.zsoltk.rf1.ui.decoration.square.TargetMarks

object DefaultSquareRenderer : SquareRenderer {

    override val decorations: List<SquareDecoration> =
        listOf(
            DefaultSquareBackground,
            DefaultHighlightSquare,
            DefaultSquarePositionLabel,
            TargetMarks
        )
}
