package com.github.zsoltk.chesso.model.game.converter

import com.github.zsoltk.chesso.model.game.state.GameState

interface Converter {

    fun preValidate(text: String): Boolean

    fun import(text: String): ImportResult

    fun export(gameState: GameState): String
}

