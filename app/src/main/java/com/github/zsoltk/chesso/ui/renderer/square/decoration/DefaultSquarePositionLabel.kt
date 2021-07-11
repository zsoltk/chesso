package com.github.zsoltk.chesso.ui.renderer.square.decoration

import com.github.zsoltk.chesso.model.board.Coordinate

object DefaultSquarePositionLabel : SquarePositionLabelSplit(
    displayFile = { coordinate -> coordinate.y == Coordinate.max.y },
    displayRank = { coordinate -> coordinate.x == Coordinate.min.x }
)
