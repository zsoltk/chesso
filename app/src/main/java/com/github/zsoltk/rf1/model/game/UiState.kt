package com.github.zsoltk.rf1.model.game

import androidx.compose.runtime.mutableStateOf
import com.github.zsoltk.rf1.model.notation.Position

class UiState {
    var selectedPosition = mutableStateOf<Position?>(null)
}
