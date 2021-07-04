package com.github.zsoltk.rf1.model.game.converter

import com.github.zsoltk.rf1.model.game.state.GameMetaInfo

data class PgnImportDataHolder(
    val metaInfo: GameMetaInfo,
    val moves: List<String>
)
