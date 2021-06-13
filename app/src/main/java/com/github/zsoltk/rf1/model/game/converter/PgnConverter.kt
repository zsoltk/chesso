package com.github.zsoltk.rf1.model.game.converter

import android.annotation.SuppressLint
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import java.text.SimpleDateFormat
import java.util.Date


object PgnConverter : Converter {

    override fun import(notation: String): GameState {
        TODO("not implemented")
    }

    @SuppressLint("SimpleDateFormat")
    override fun export(gameState: GameState): String {
        val sb = StringBuilder()
        val sdf = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(Date())
        val player1 = "Player 1"
        val player2 = "Player 2"

        sb.append("[Event \"Chesso game\"]\n")
        sb.append("[Date \"$currentDate\"]\n")
        sb.append("[White \"$player1\"]\n")
        sb.append("[Black \"$player2\"]\n")
        when (gameState.resolution) {
            Resolution.IN_PROGRESS -> {}
            Resolution.CHECKMATE -> {
                val result = if (gameState.states.last().toMove == BLACK) "1-0" else "0-1"
                val winner = if (gameState.states.last().toMove == BLACK) player1 else player2
                sb.append("[Result \"$result\"]\n")
                sb.append("[Termination \"$winner won by checkmate\"]\n")
            }
            Resolution.STALEMATE -> {
                sb.append("[Result \"½ - ½\"]\n")
                sb.append("[Termination \"Stalemate\"]\n")
            }
            Resolution.DRAW_BY_REPETITION -> {
                sb.append("[Result \"½ - ½\"]\n")
                sb.append("[Termination \"Draw by repetition\"]\n")
            }
        }

        gameState.states.forEachIndexed { i, state ->
            val move = (i / 2) + 1
            state.move?.let {
                if (i % 2  == 0) sb.append("$move. ")
                sb.append(it.toString(
                    useFigurineNotation = false,
                    includeResult = false
                ))
                sb.append(" ")
            }
        }

        return sb.toString()
    }
}
