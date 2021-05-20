package com.github.zsoltk.rf1.model.board

import com.github.zsoltk.rf1.model.notation.Position.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    @Test
    fun position_is_correct() {
        val square = Square(3, 3)
        assertEquals(c3, square.position)
    }

    @Test
    fun delta_valid_case_1() {
        val square = Square(d4)
        val result: UnboundSquare = square + Delta(1, 1)
        assertTrue(result is Square)
        assertEquals(e5, (result as Square).position)
    }

    @Test
    fun delta_valid_case_2() {
        val square = Square(d4)
        val result: UnboundSquare = square + Delta(-1, -1)
        assertTrue(result is Square)
        assertEquals(c3, (result as Square).position)
    }

    @Test
    fun delta_invalid_case_1() {
        val square = Square(h8)
        val result = square + Delta(0, 1)
        assertTrue(result is Invalid)
    }

    @Test
    fun delta_invalid_case_2() {
        val square = Square(h8)
        val result = square + Delta(1, 0)
        assertTrue(result is Invalid)
    }

    @Test
    fun delta_invalid_case_3() {
        val square = Square(a1)
        val result = square + Delta(-1, 0)
        assertTrue(result is Invalid)
    }

    @Test
    fun delta_invalid_case_4() {
        val square = Square(a1)
        val result = square + Delta(0, -1)
        assertTrue(result is Invalid)
    }
}
