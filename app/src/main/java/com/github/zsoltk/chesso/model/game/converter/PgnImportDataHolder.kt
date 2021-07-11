package com.github.zsoltk.chesso.model.game.converter

import com.github.zsoltk.chesso.model.game.state.GameMetaInfo

data class PgnImportDataHolder(
    val metaInfo: GameMetaInfo,
    val moves: List<String>
)
