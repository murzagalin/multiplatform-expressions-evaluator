package com.murzagalin.evaluator.integration

import com.murzagalin.evaluator.evaluateDouble
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BracketsTests {

    @Test
    fun sum_in_brackets_multiplied() {
        assertEquals(((351 + 621) * 43).toDouble(), "(351+621)*43".evaluateDouble())
        assertEquals((351 + 621 * 43).toDouble(), "351+621*43".evaluateDouble())
    }

    @Test
    fun division_powered() {
        assertEquals((12 + 32).toDouble().pow(3), "(12+32)^3".evaluateDouble())
        assertEquals(12 + 32.0.pow(3), "12+32^3".evaluateDouble())
    }

    @Test
    fun just_brackets() {
        assertFailsWith(IllegalArgumentException::class, "malformed expression") { "()".evaluateDouble() }
    }
}