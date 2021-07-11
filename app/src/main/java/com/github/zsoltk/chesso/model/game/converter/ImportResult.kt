package com.github.zsoltk.chesso.model.game.converter

import com.github.zsoltk.chesso.model.game.state.GameState

sealed class ImportResult {
    class ValidationError(val msg: String) : ImportResult()
    class ImportedGame(val gameState: GameState) : ImportResult()
}
