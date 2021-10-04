package com.murzagalin.evaluator.integration

import com.murzagalin.evaluator.evaluateDouble
import kotlin.math.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FunctionsTests {

    @Test
    fun simple_functions_calls() {
        assertEquals(cos(1.0), "cos(1.0)".evaluateDouble())
        assertEquals(ln(2.0), "ln(2.0)".evaluateDouble())
        assertEquals(sin(1.0), "sin(1.0)".evaluateDouble())
        assertEquals(log(2.0, 3.0), "log(2.0, 3.0)".evaluateDouble())
        assertEquals(sin(1.0), "sin(min(1, 2))".evaluateDouble())
        assertEquals(sin(2.0), "sin(max(1, 2))".evaluateDouble())
        assertEquals(sin(1.0), "min(sin(1), max(2, 3, 4))".evaluateDouble())
        assertEquals(2.0, "max(sin(1), min(2, 3, 4))".evaluateDouble())
    }

    @Test
    fun expressions_with_functions() {
        assertEquals(1 + cos(1.0*12 + 3), "1 + cos(1.0*12+3)".evaluateDouble())
        assertEquals(2 * ln(2.0.pow(3)), "2*ln(2.0^3)".evaluateDouble())
        assertEquals(4*sin(1.0*ln(12.0)), "4*sin(1.0*ln(12))".evaluateDouble())
        assertEquals(5*sin(1.0+99.0+234)+-1, "5*sin(1.0+99.0 +234)+-1".evaluateDouble())
        assertEquals(37.0, "3^3 + 5 * max(sin(1), min(2, 3, 4))".evaluateDouble())
    }

    @Test
    fun evaluated_arguments_in_multiarg_functions() {
        assertEquals(log(2*2.0,3.0), "log(2*2, 3)".evaluateDouble())
        assertEquals(6*log(2.0*12, 3.0.pow(4)), "6*log(2.0*12, 3.0^4)".evaluateDouble())
    }

    @Test
    fun wrong_number_of_arguments() {
        assertFailsWith<IllegalArgumentException> { "min()".evaluateDouble() }
        assertFailsWith<IllegalArgumentException> { "cos(2, 3)".evaluateDouble() }
        assertFailsWith<IllegalArgumentException> { "log(2)".evaluateDouble() }
    }

    @Test
    fun wrong_arguments_types() {
        assertFailsWith<IllegalArgumentException> { "min(true, false)".evaluateDouble() }
        assertFailsWith<IllegalArgumentException> { "cos(true)".evaluateDouble() }
        assertFailsWith<IllegalArgumentException> { "ln(var)".evaluateDouble(mapOf("var" to true)) }
    }
}