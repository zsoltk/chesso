package com.github.zsoltk.rf1.model.board

import com.github.zsoltk.rf1.model.notation.AlgebraicNotation.f8
import org.junit.Assert.assertEquals
import org.junit.Test

class SquareTest {

    @Test
    fun toString_is_correct_a1() {
        val square = Square(1, 1)
        assertEquals("a1", square.toString())
    }

    @Test
    fun toString_is_correct_d4() {
        val square = Square(4, 4)
        assertEquals("d4", square.toString())
    }

    @Test
    fun secondaryConstructor_file_parsed_correctly() {
        val square = Square(File.e, Rank.r4)
        assertEquals(5, square.file)
    }

    @Test
    fun secondaryConstructor_rank_parsed_correctly() {
        val square = Square(File.e, Rank.r4)
        assertEquals(4, square.rank)
    }

    @Test
    fun algebraicNotation_file_parsed_correctly() {
        val square = Square(f8)
        assertEquals(6, square.file)
    }

    @Test
    fun algebraicNotation_rank_parsed_correctly() {
        val square = Square(f8)
        assertEquals(8, square.rank)
    }
}
