package com.github.zsoltk.rf1.model.game.converter

import android.annotation.SuppressLint
import com.github.zsoltk.rf1.model.board.File
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.KingSideCastle
import com.github.zsoltk.rf1.model.move.Promotion
import com.github.zsoltk.rf1.model.move.QueenSideCastle
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import java.text.SimpleDateFormat
import java.util.Date

object PgnConverter : Converter {

    override fun import(text: String): GameState {
        val target = text
            .replace("[\n\r]".toRegex(), "")
            .replace("(1-0|0-1|1/2-1/2)\$".toRegex(), "")
            .trim()

        // Log.d("PGN normalised", target)
        val tagsPattern = """\[(\w+)\s"(.*?)"\]""".toRegex()
        val tagsResults = tagsPattern.findAll(target)
        val tags = tagsResults
            .associate { it.groupValues[1] to it.groupValues[2] }

        val moveChars = """[\w-=+#]"""
        val movesPattern = """\d+\.\s($moveChars+)(\s($moveChars+))?""".toRegex()
        val movesResults = movesPattern.findAll(target)
        val moves = movesResults
            .flatMap { listOf(it.groupValues[1].trim(), it.groupValues[2].trim()) }
            .toList()
            .filter { it.isNotBlank() }

        // Log.d("PGN", "$moves")

        var gamePlayState = GamePlayState()
        val gameController = GameController(
            { gamePlayState },
            { gamePlayState = it }
        )
        moves.forEach { moveText ->
            parseMove(moveText, gamePlayState.gameState).let { move ->
                gameController.applyMove(move)
                // Log.d("PGN", "$moveText = $move")
            }

        }

        return gamePlayState.gameState
    }

    private fun parseMove(s: String, gameState: GameState): BoardMove {
        val state = gameState.currentSnapshotState

        if (s == "O-O") {
            return state.allLegalMoves
                .find {
                    it.move is KingSideCastle && it.move.piece.set == gameState.toMove
                } ?: error("Invalid state. Can't castle kingside for ${gameState.toMove}")
        }
        if (s == "O-O-O") {
            return state.allLegalMoves
                .find {
                    it.move is QueenSideCastle && it.move.piece.set == gameState.toMove
                } ?: error("Invalid state. Can't castle queenside for ${gameState.toMove}")
        }

        val pattern = "([NBRQK])?([abcdefgh])?([1-8])?x?([abcdefgh])([1-8])(=[KBRQ])?[+#]?".toRegex()
        val result = pattern.find(s) ?: error("Can't parse move: $s")

        val piece = result.groupValues[1]
        val fromFileChar = result.groupValues[2]
        val fromFile = if (fromFileChar == "") null else File.valueOf(fromFileChar)
        val fromRank = result.groupValues[3]
        val toFileChar = result.groupValues[4]
        val toFile = File.valueOf(toFileChar)
        val toRank = result.groupValues[5].toInt()
        val toPosition = Position.from(toFile.ordinal + 1, toRank)
        val promotion = result.groupValues[6]

        val filtered = state.allLegalMoves.filter {
            it.piece.set == state.toMove &&
                it.piece.textSymbol == piece &&
                it.to == toPosition &&
                (fromFile == null || fromFile.ordinal + 1 == it.from.file) &&
                (fromRank == "" || fromRank == it.from.rank.toString()) &&
                (promotion == "" || (it.consequence is Promotion && it.consequence.piece.textSymbol == promotion[1].toString()))
        }
        when (filtered.size) {
            0 -> error("Invalid state when parsing $s. No legal moves exist to $toPosition for ${gameState.toMove}")
            1 -> return filtered[0]
            else -> error("Ambiguity when parsing $s. Too many moves exist to $toPosition for ${gameState.toMove}: $filtered")
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun export(gameState: GameState): String {
        val sb = StringBuilder()
        val sdf = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(Date())
        val player1 = "Player 1"
        val player2 = "Player 2"

        sb.append("[Event \"Chesso game\"]\n")
        sb.append("[Site \"Chesso app\"]\n")
        sb.append("[Date \"$currentDate\"]\n")
        sb.append("[White \"$player1\"]\n")
        sb.append("[Black \"$player2\"]\n")
        when (gameState.resolution) {
            Resolution.IN_PROGRESS -> {
            }
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
                if (i % 2 == 0) sb.append("$move. ")
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
