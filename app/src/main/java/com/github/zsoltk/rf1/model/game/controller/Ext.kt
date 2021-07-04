package com.github.zsoltk.rf1.model.game.controller

import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.game.state.GameMetaInfo
import com.github.zsoltk.rf1.model.piece.Set

fun GameMetaInfo.withResolution(resolution: Resolution, lastToMove: Set): GameMetaInfo =
    when (resolution) {
        Resolution.IN_PROGRESS -> this
        Resolution.CHECKMATE -> {
            val result = if (lastToMove == Set.BLACK) "1-0" else "0-1"
            val winner = if (lastToMove == Set.BLACK) white else black
            copy(
                tags = tags
                    .plus(GameMetaInfo.KEY_RESULT to result)
                    .plus(GameMetaInfo.KEY_TERMINATION to "$winner won by checkmate")
            )
        }
        Resolution.STALEMATE -> {
            copy(
                tags = tags
                    .plus(GameMetaInfo.KEY_RESULT to "½ - ½")
                    .plus(GameMetaInfo.KEY_TERMINATION to "Stalemate")
            )
        }
        Resolution.DRAW_BY_REPETITION -> {
            copy(
                tags = tags
                    .plus(GameMetaInfo.KEY_RESULT to "½ - ½")
                    .plus(GameMetaInfo.KEY_TERMINATION to "Draw by repetition")
            )
        }
        Resolution.INSUFFICIENT_MATERIAL -> {
            copy(
                tags = tags
                    .plus(GameMetaInfo.KEY_RESULT to "½ - ½")
                    .plus(GameMetaInfo.KEY_TERMINATION to "Draw by insufficient material")
            )
        }
    }
