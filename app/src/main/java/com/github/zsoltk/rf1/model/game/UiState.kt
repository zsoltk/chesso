package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.mutableStateOf
import com.github.zsoltk.rf1.model.notation.Position
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class UiState {
    var selectedPosition by mutableStateOf<Position?>(null)
}
