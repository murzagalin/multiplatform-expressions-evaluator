package com.murzagalin.evaluator.integration

import com.murzagalin.evaluator.Evaluator
import kotlin.math.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class VariablesTests {

    private val evaluator = Evaluator()

    @Test
    fun expressions_with_functions_and_variables() {
        assertEquals(
            1 + cos(1.0*12 + 3),
            evaluator.evaluateDouble("1 + cos(x*12+y)", mapOf("x" to 1.0, "y" to 3.0))
        )
        assertEquals(
            2 * ln(2.0.pow(3)),
            evaluator.evaluateDouble("2*ln(2.0^x)", mapOf("x" to 3.0))
        )
        assertEquals(
            4* sin(2.0* ln(12.0)),
            evaluator.evaluateDouble("x*sin(y*ln(z))", mapOf("x" to 4.0, "y" to 2.0, "z" to 12.0))
        )
        assertEquals(
            5* sin(1.0+99.0+234) +-1,
            evaluator.evaluateDouble(
                "var1*sin(1.0+var2 +234)+var3",
                mapOf("var1" to 5.0, "var2" to 99.0, "var3" to -1.0)
            )
        )
        assertEquals(
            5* sin(1.0+99.0+234) +1,
            evaluator.evaluateDouble(
                "var1*sin(1.0+var2 +234)+-var3",
                mapOf("var1" to 5.0, "var2" to 99.0, "var3" to -1.0)
            )
        )
    }

    @Test
    fun fail_resolving_test() {
        assertFailsWith<IllegalArgumentException>("Could not resolve variable 'y'") {
            evaluator.evaluateDouble("1 + cos(x*12+y)", mapOf("x" to 1.0))
        }
    }
}