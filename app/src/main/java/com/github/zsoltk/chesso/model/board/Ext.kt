package com.github.zsoltk.chesso.model.board

fun idx(file: Int, rank: Int): Int =
    (file - 1) * 8 + (rank - 1)

fun validate(file: Int, rank: Int) {
    require(file >= 1)
    require(file <= 8)
    require(rank >= 1)
    require(rank <= 8)
}

fun Position.isLightSquare(): Boolean =
    (ordinal + file % 2) % 2 == 0

fun Position.isDarkSquare(): Boolean =
    (ordinal + file % 2) % 2 == 1

fun Position.toCoordinate(isFlipped: Boolean): Coordinate = if (isFlipped)
    Coordinate(
        x = Coordinate.max.x - file + 1,
        y = rank.toFloat(),
    ) else Coordinate(
        x = file.toFloat(),
        y = Coordinate.max.y - rank + 1,
    )
