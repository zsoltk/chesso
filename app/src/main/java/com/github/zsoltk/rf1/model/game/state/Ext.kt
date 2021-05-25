package com.github.zsoltk.rf1.model.game.state

fun List<BoardState>.hasThreefoldRepetition(): Boolean =
    map { it.hashCode() }
        .groupBy { it }
        .map { it.value.size }
        .any { it > 2 }
