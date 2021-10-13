package com.github.murzagalin.evaluator.integration

import com.github.murzagalin.evaluator.Evaluator
import kotlin.test.Test
import kotlin.test.assertEquals

class UnaryOperatorsTests {

    private val evaluator = Evaluator()

    @Test
    fun several_unary_minuses() {
        assertEquals(5.0, evaluator.evaluateDouble("--5"))
        assertEquals(5.0, evaluator.evaluateDouble("-(-5)"))
        assertEquals(-5.0, evaluator.evaluateDouble("--(-5)"))
        assertEquals(5.0, evaluator.evaluateDouble("+(-(-5))"))
        assertEquals(-5.0, evaluator.evaluateDouble("---5"))
    }

    @Test
    fun unary_minus_and_plus_with_number() {
        assertEquals(-5.0, evaluator.evaluateDouble("-5"))
        assertEquals(5.0, evaluator.evaluateDouble("5"))
    }

    @Test
    fun unary_minus_and_plus_before_brackets() {
        assertEquals(-(3+55).toDouble(), evaluator.evaluateDouble("-(3+55)"))
        assertEquals((3+55).toDouble(), evaluator.evaluateDouble("(3+55)"))
    }

    @Test
    fun unary_minus_and_plus_with_brackets() {
        assertEquals(4-(3+55).toDouble(), evaluator.evaluateDouble("4+-(3+55)"))
        assertEquals(4-(3+55).toDouble(), evaluator.evaluateDouble("4-+(3+55)"))
    }
}