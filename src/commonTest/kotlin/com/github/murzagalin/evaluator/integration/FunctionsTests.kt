package com.github.murzagalin.evaluator.integration

import com.github.murzagalin.evaluator.Evaluator
import kotlin.math.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FunctionsTests {

    private val evaluator = Evaluator()

    @Test
    fun simple_functions_calls() {
        assertEquals(cos(1.0), evaluator.evaluateDouble("cos(1.0)"))
        assertEquals(ln(2.0), evaluator.evaluateDouble("ln(2.0)"))
        assertEquals(sin(1.0), evaluator.evaluateDouble("sin(1.0)"))
        assertEquals(log(2.0, 3.0), evaluator.evaluateDouble("log(2.0, 3.0)"))
        assertEquals(sin(1.0), evaluator.evaluateDouble("sin(min(1, 2))"))
        assertEquals(sin(2.0), evaluator.evaluateDouble("sin(max(1, 2))"))
        assertEquals(sin(1.0), evaluator.evaluateDouble("min(sin(1), max(2, 3, 4))"))
        assertEquals(2.0, evaluator.evaluateDouble("max(sin(1), min(2, 3, 4))"))
        assertEquals(-1.0, evaluator.evaluateDouble("ceil(-1.5)"))
        assertEquals(-2.0, evaluator.evaluateDouble("floor(-1.5)"))
    }

    @Test
    fun expressions_with_functions() {
        assertEquals(1 + cos(1.0*12 + 3), evaluator.evaluateDouble("1 + cos(1.0*12+3)"))
        assertEquals(2 * ln(2.0.pow(3)), evaluator.evaluateDouble("2*ln(2.0^3)"))
        assertEquals(4*sin(1.0*ln(12.0)), evaluator.evaluateDouble("4*sin(1.0*ln(12))"))
        assertEquals(5*sin(1.0+99.0+234)+-1, evaluator.evaluateDouble("5*sin(1.0+99.0 +234)+-1"))
        assertEquals(37.0, evaluator.evaluateDouble("3^3 + 5 * max(sin(1), min(2, 3, 4))"))
    }

    @Test
    fun evaluated_arguments_in_multiarg_functions() {
        assertEquals(log(2*2.0,3.0), evaluator.evaluateDouble("log(2*2, 3)"))
        assertEquals(6*log(2.0*12, 3.0.pow(4)), evaluator.evaluateDouble("6*log(2.0*12, 3.0^4)"))
    }

    @Test
    fun wrong_number_of_arguments() {
        assertFailsWith<IllegalArgumentException> { evaluator.evaluateDouble("min()") }
        assertFailsWith<IllegalArgumentException> { evaluator.evaluateDouble("cos(2, 3)") }
        assertFailsWith<IllegalArgumentException> { evaluator.evaluateDouble("log(2)") }
    }

    @Test
    fun wrong_arguments_types() {
        assertFailsWith<IllegalArgumentException> { evaluator.evaluateDouble("min(true, false)") }
        assertFailsWith<IllegalArgumentException> { evaluator.evaluateDouble("cos(true)") }
        assertFailsWith<IllegalArgumentException> { evaluator.evaluateDouble("ln(var)", mapOf("var" to true)) }
    }
}