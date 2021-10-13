package com.github.murzagalin.evaluator.integration

import com.github.murzagalin.evaluator.Evaluator
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BracketsTests {
    private val evaluator = Evaluator()

    @Test
    fun sum_in_brackets_multiplied() {
        assertEquals(((351 + 621) * 43).toDouble(), evaluator.evaluateDouble("(351+621)*43"))
        assertEquals((351 + 621 * 43).toDouble(), evaluator.evaluateDouble("351+621*43"))
    }

    @Test
    fun division_powered() {
        assertEquals((12 + 32).toDouble().pow(3), evaluator.evaluateDouble("(12+32)^3"))
        assertEquals(12 + 32.0.pow(3), evaluator.evaluateDouble("12+32^3"))
    }

    @Test
    fun just_brackets() {
        assertFailsWith(IllegalArgumentException::class, "malformed expression") {
            evaluator.evaluateDouble("()")
        }
    }
}