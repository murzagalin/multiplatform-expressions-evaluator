package com.github.murzagalin.evaluator.integration

import com.github.murzagalin.evaluator.Evaluator
import kotlin.math.E
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class ConstantsTest {

    private val evaluator = Evaluator()

    @Test
    fun simple_constants() {
        assertEquals(E, evaluator.evaluateDouble("e"))
        assertEquals(PI, evaluator.evaluateDouble("pi"))
    }

    @Test
    fun expressions_with_constants() {
        assertEquals(-1.0, evaluator.evaluateDouble("cos(pi)"), absoluteTolerance=0.000000001)
        assertEquals(1.0, evaluator.evaluateDouble("cos(2*pi)"), absoluteTolerance=0.000000001)
        assertEquals(0.0, evaluator.evaluateDouble("sin(0)"), absoluteTolerance=0.000000001)
        assertEquals(0.0, evaluator.evaluateDouble("sin(pi)"), absoluteTolerance=0.000000001)
        assertEquals(1.0, evaluator.evaluateDouble("sin(pi / 2)"), absoluteTolerance=0.000000001)
        assertEquals(
            1.0,
            evaluator.evaluateDouble("sin(var)^2+cos(var)^2", mapOf("var" to 2.0)),
            absoluteTolerance=0.000000001
        )

        assertEquals(3.0, evaluator.evaluateDouble("ln(e^3)"))
    }
}