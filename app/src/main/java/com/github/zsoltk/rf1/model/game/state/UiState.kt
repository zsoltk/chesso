package com.github.zsoltk.rf1.model.game.state

import androidx.compose.runtime.mutableStateOf
import com.github.zsoltk.rf1.model.board.Position
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class UiState {
    var selectedPosition by mutableStateOf<Position?>(null)
}
