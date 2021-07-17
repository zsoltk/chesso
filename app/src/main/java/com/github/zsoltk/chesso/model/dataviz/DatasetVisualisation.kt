package com.github.zsoltk.chesso.model.dataviz

import android.os.Parcelable
import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState

interface DatasetVisualisation : Parcelable {

    val name: Int

    val minValue: Int

    val maxValue: Int

    fun dataPointAt(position: Position, state: GameSnapshotState, cache: MutableMap<Any, Any>): Datapoint?
}

