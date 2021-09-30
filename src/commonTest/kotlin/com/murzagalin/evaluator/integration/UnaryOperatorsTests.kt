package com.murzagalin.evaluator.integration

import com.murzagalin.evaluator.evaluateDouble
import kotlin.test.Test
import kotlin.test.assertEquals

class UnaryOperatorsTests {

    @Test
    fun several_unary_minuses() {
        assertEquals(5.0, "--5".evaluateDouble())
        assertEquals(5.0, "-(-5)".evaluateDouble())
        assertEquals(-5.0, "--(-5)".evaluateDouble())
        assertEquals(5.0, "+(-(-5))".evaluateDouble())
        assertEquals(-5.0, "---5".evaluateDouble())
    }

    @Test
    fun unary_minus_and_plus_with_number() {
        assertEquals(-5.0, "-5".evaluateDouble())
        assertEquals(5.0, "5".evaluateDouble())
    }

    @Test
    fun unary_minus_and_plus_before_brackets() {
        assertEquals(-(3+55).toDouble(), "-(3+55)".evaluateDouble())
        assertEquals((3+55).toDouble(), "(3+55)".evaluateDouble())
    }

    @Test
    fun unary_minus_and_plus_with_brackets() {
        assertEquals(4-(3+55).toDouble(), "4+-(3+55)".evaluateDouble())
        assertEquals(4-(3+55).toDouble(), "4-+(3+55)".evaluateDouble())
    }
}