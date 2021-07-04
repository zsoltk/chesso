package com.github.zsoltk.rf1.model.game.converter

import com.github.zsoltk.rf1.model.game.state.GameState

sealed class ImportResult {
    class ValidationError(val msg: String) : ImportResult()
    class ImportedGame(val gameState: GameState) : ImportResult()
}
