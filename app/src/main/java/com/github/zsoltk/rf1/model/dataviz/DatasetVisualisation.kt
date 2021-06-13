package com.github.zsoltk.rf1.model.dataviz

import android.os.Parcelable
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState

interface DatasetVisualisation : Parcelable {

    val name: String

    val minValue: Int

    val maxValue: Int

    fun dataPointAt(position: Position, state: GameSnapshotState): Datapoint?
}

