package com.murzagalin.evaluator.integration

import com.murzagalin.evaluator.Evaluator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleTests {

    private val evaluator = Evaluator()

    @Test
    fun simple_sum() {
        val testsNum = 100
        val random = Random.Default

        for (testIx in 0 until testsNum) {
            val operandsCount = random.nextInt(5, 15)
            val expressionBuilder = StringBuilder()
            var result = 0
            for (operandIx in 0 until operandsCount) {
                val newOperand = random.nextInt(0, 99999)
                expressionBuilder.append(newOperand).append('+')
                result += newOperand
            }

            val expression = expressionBuilder.removeSuffix("+").toString()

            assertEquals(
                result.toDouble(),
                evaluator.evaluateDouble(expression),
                "wrong result in expression $expression"
            )
        }
    }

    @Test
    fun simple_mult() {
        val testsNum = 100
        val random = Random.Default

        for (testIx in 0 until testsNum) {
            val operandsCount = random.nextInt(2, 6)
            val expressionBuilder = StringBuilder()
            var result = 1
            for (operandIx in 0 until operandsCount) {
                val newOperand = random.nextInt(10, 29)
                expressionBuilder.append(newOperand).append('*')
                result *= newOperand
            }

            val expression = expressionBuilder.removeSuffix("*").toString()

            assertEquals(
                result.toDouble(),
                evaluator.evaluateDouble(expression),
                "wrong result in expression $expression"
            )
        }
    }

    @Test
    fun simple_division() {
        assertEquals(4.0, evaluator.evaluateDouble("8/2"))
        assertEquals(3.0, evaluator.evaluateDouble("12/4"))
        assertEquals(0.5, evaluator.evaluateDouble("8/16"))
        assertEquals(0.25, evaluator.evaluateDouble("8/16/2"))
        assertEquals(0.125, evaluator.evaluateDouble("8/16/2/2"))
    }

    @Test
    fun simple_mod() {
        assertEquals(-1.0, evaluator.evaluateDouble("-5%4"))
        assertEquals(0.0, evaluator.evaluateDouble("8%2"))
        assertEquals(2.0, evaluator.evaluateDouble("12%5"))
        assertEquals(8.0, evaluator.evaluateDouble("8%16"))
        assertEquals(2.0, evaluator.evaluateDouble("8%16%3"))
        assertEquals(0.16, evaluator.evaluateDouble("2.5%0.26"), absoluteTolerance = 0.000000000000001)
        assertEquals(0.5, evaluator.evaluateDouble("0.5%0.75"))
        assertEquals(0.1, evaluator.evaluateDouble("2.5%0.6"), absoluteTolerance = 0.000000000000001)
    }

    @Test
    fun simple_subtraction() {
        assertEquals(6.0, evaluator.evaluateDouble("8-2"))
        assertEquals(8.0, evaluator.evaluateDouble("12-4"))
        assertEquals(-8.0, evaluator.evaluateDouble(" 8 - 16 "))
        assertEquals(-10.0, evaluator.evaluateDouble("8-16-2"))
        assertEquals(-12.0, evaluator.evaluateDouble("8-16-2-2"))
    }

    @Test
    fun simple_exponentiation() {
        assertEquals(64.0, evaluator.evaluateDouble("8^2"))
        assertEquals(1728.0, evaluator.evaluateDouble("12^3"))
        assertEquals(256.0, evaluator.evaluateDouble("2^8"))
        assertEquals(27.0, evaluator.evaluateDouble("3^3"))
        assertEquals(512.0, evaluator.evaluateDouble("8^3"))
    }

    @Test
    fun unary_minus_pow_precedence() {
        assertEquals(-4.0, evaluator.evaluateDouble("-2^2"))
        assertEquals(4.0, evaluator.evaluateDouble("(-2)^2"))
    }
}