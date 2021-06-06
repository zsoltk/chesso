package com.github.zsoltk.rf1.ui.decoration.square

import com.github.zsoltk.rf1.model.board.Coordinate

object DefaultSquarePositionLabel : SquarePositionLabelSplit(
    displayFile = { coordinate -> coordinate.y == Coordinate.max.y },
    displayRank = { coordinate -> coordinate.x == Coordinate.min.x }
)
