package com.github.zsoltk.rf1.model.board

import com.github.zsoltk.rf1.model.board.File.h
import com.github.zsoltk.rf1.model.board.Rank.r4
import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.h4
import org.junit.Assert.assertEquals
import org.junit.Test

class FileExtTest {

    @Test
    fun operator_overloading_int_results_in_correct_algebraic_notation() {
        assertEquals(h4, h[4])
    }

    @Test
    fun operator_overloading_rank_results_in_correct_algebraic_notation() {
        assertEquals(h4, h[r4])
    }
}
