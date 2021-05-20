package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.mutableStateOf
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Set

class Game {

    val player: Set = Set.WHITE // Fixed value for now

    var selectedPosition = mutableStateOf<Position?>(null)
}