package com.github.zsoltk.rf1.model.game.converter

import com.github.zsoltk.rf1.model.board.File
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.state.GameMetaInfo
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.KingSideCastle
import com.github.zsoltk.rf1.model.move.Promotion
import com.github.zsoltk.rf1.model.move.QueenSideCastle

object PgnConverter : Converter {

    private const val MOVE_CASTLE_KINGSIDE = "O-O"

    private const val MOVE_CASTLE_QUEENSIDE = "O-O-O"

    private val MOVE_REGEX = "([NBRQK])?([abcdefgh])?([1-8])?x?([abcdefgh])([1-8])(=[KBRQ])?[+#]?".toRegex()

    override fun preValidate(text: String): Boolean {
        val moves = extractData(text).moves

        return moves.isNotEmpty() && moves.all { validateMoveText(it) }
    }

    private fun validateMoveText(s: String): Boolean =
        (s == MOVE_CASTLE_KINGSIDE || s == MOVE_CASTLE_QUEENSIDE || MOVE_REGEX.matchEntire(s) != null)

    override fun import(text: String): GameState {
        val pgnImportDataHolder = extractData(text)
        var gamePlayState = GamePlayState(
            gameState = GameState(
                gameMetaInfo = pgnImportDataHolder.metaInfo
            )
        )
        val gameController = GameController(
            { gamePlayState },
            { gamePlayState = it }
        )
        pgnImportDataHolder.moves.forEach { moveText ->
            createMove(moveText, gamePlayState.gameState).let { move ->
                gameController.applyMove(move)
            }

        }

        return gamePlayState.gameState
    }

    private fun extractData(text: String): PgnImportDataHolder {
        val target = text
            .replace("[\n\r]".toRegex(), "")
            .replace("(1-0|0-1|1/2-1/2)\$".toRegex(), "")
            .trim()

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

        return PgnImportDataHolder(
            metaInfo = GameMetaInfo(tags = tags),
            moves = moves
        )
    }

    private fun createMove(s: String, gameState: GameState): BoardMove {
        val state = gameState.currentSnapshotState

        if (s == MOVE_CASTLE_KINGSIDE) {
            return state.allLegalMoves
                .find {
                    it.move is KingSideCastle && it.move.piece.set == gameState.toMove
                } ?: error("Invalid state. Can't castle kingside for ${gameState.toMove}")
        }
        if (s == MOVE_CASTLE_QUEENSIDE) {
            return state.allLegalMoves
                .find {
                    it.move is QueenSideCastle && it.move.piece.set == gameState.toMove
                } ?: error("Invalid state. Can't castle queenside for ${gameState.toMove}")
        }

        val result = MOVE_REGEX.find(s) ?: error("Can't parse move: $s")
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

    override fun export(gameState: GameState): String {
        val sb = StringBuilder()
        listOf(
            GameMetaInfo.KEY_EVENT,
            GameMetaInfo.KEY_SITE,
            GameMetaInfo.KEY_DATE,
            GameMetaInfo.KEY_WHITE,
            GameMetaInfo.KEY_BLACK,
            GameMetaInfo.KEY_RESULT,
            GameMetaInfo.KEY_TERMINATION,
        ) .forEach { tag ->
            gameState.gameMetaInfo.tags[tag]?.let { valueForTag ->
                sb.append("[$tag \"$valueForTag\"]\n")
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
