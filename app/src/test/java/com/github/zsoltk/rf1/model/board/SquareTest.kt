package com.github.zsoltk.rf1.model.board

import com.github.zsoltk.rf1.model.board.Position.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SquareTest {

    @Test
    fun toString_is_correct_d4() {
        val square = Square(d4)
        assertEquals("d4", square.toString())
    }

    @Test
    fun isDark_correct_a1() {
        val square = Square(a1)
        assertTrue(square.isDark)
    }

    @Test
    fun isDark_correct_a2() {
        val square = Square(a2)
        assertFalse(square.isDark)
    }

    @Test
    fun isDark_correct_b1() {
        val square = Square(b1)
        assertFalse(square.isDark)
    }

    @Test
    fun isDark_correct_b2() {
        val square = Square(b2)
        assertTrue(square.isDark)
    }
}
