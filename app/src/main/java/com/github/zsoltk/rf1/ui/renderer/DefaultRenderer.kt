package com.github.zsoltk.rf1.ui.renderer

import com.github.zsoltk.rf1.ui.decoration.DefaultHighlightSquare
import com.github.zsoltk.rf1.ui.decoration.DefaultSquareBackground
import com.github.zsoltk.rf1.ui.decoration.DefaultSquarePositionLabel
import com.github.zsoltk.rf1.ui.decoration.SquareDecoration
import com.github.zsoltk.rf1.ui.decoration.TargetMarks

object DefaultRenderer : Renderer {

    override val decorations: List<SquareDecoration> =
        listOf(
            DefaultSquareBackground,
            DefaultHighlightSquare,
            DefaultSquarePositionLabel,
            TargetMarks
        )
}
