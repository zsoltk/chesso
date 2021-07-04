package com.github.zsoltk.rf1.model.game.state

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date

@Parcelize
data class GameMetaInfo(
    val tags: Map<String, String>,
) : Parcelable {

    operator fun get(key: String): String? =
        tags[key]

    val event: String? = get(KEY_EVENT)

    val site: String? = get(KEY_SITE)

    val date: String? = get(KEY_DATE)

    val white: String? = get(KEY_WHITE)

    val black: String? = get(KEY_BLACK)

    val result: String? = get(KEY_RESULT)

    val termination: String? = get(KEY_TERMINATION)

    companion object {
        const val KEY_EVENT = "Event"
        const val KEY_SITE = "Site"
        const val KEY_DATE = "Date"
        const val KEY_WHITE = "White"
        const val KEY_BLACK = "Black"
        const val KEY_RESULT = "Result"
        const val KEY_TERMINATION = "Termination"

        @SuppressLint("SimpleDateFormat")
        fun createWithDefaults(): GameMetaInfo =
            GameMetaInfo(
                tags = mapOf(
                    KEY_EVENT to "Chesso game",
                    KEY_SITE to "Chesso app",
                    KEY_DATE to SimpleDateFormat("yyyy-M-dd").format(Date()),
                    KEY_WHITE to "Player 1",
                    KEY_BLACK to "Player 2",
                )
            )
    }
}
