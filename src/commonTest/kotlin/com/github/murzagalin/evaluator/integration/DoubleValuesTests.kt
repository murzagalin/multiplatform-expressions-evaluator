package com.github.murzagalin.evaluator.integration

import com.github.murzagalin.evaluator.Evaluator
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals

class DoubleValuesTests {

    private val evaluator = Evaluator()

    @Test
    fun simple_value() {
        assertEquals(3214.235, evaluator.evaluateDouble("3214.235"))
    }

    @Test
    fun sum() {
        assertEquals(3214.235+23576.1245+375.23576+1756.135, evaluator.evaluateDouble("3214.235+23576.1245+375.23576+1756.135"))
    }

    @Test
    fun double_with_int() {
        assertEquals(23+0.123*(124/60.124), evaluator.evaluateDouble("23+0.123*(124/60.124)"))
    }

    @Test
    fun expressions() {
        assertEquals(3 * 23.toDouble().pow(3.2), evaluator.evaluateDouble("23^3.2*3"))
    }

    @Test
    fun complex_expression() {
        assertEquals(
            2728.0,
            evaluator.evaluateDouble("(0.341 * 8000.0) / (1 - (1 + 0.341) ^ -84)"),
            absoluteTolerance = 0.000001
        )
    }
}