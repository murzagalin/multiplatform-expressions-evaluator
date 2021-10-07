package com.murzagalin.evaluator.evaluator

import com.murzagalin.evaluator.DoubleEvaluator
import com.murzagalin.evaluator.PreprocessedExpression
import com.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertEquals

class VariableTest {

    private val subject = DoubleEvaluator()

    @Test
    fun just_variable_test() {
        val rawExpression = listOf(Token.Operand.Variable("var1"))
        val expression = PreprocessedExpression(rawExpression)
        val values = mapOf("var1" to 3.0)
        assertEquals(
            3.0,
            subject.evaluate(expression, values)
        )
    }

    @Test
    fun multiplication_of_two_variables() {
        val rawExpression = listOf(
            Token.Operand.Variable("x"),
            Token.Operand.Variable("y"),
            Token.Operator.Multiplication
        )
        val expression = PreprocessedExpression(rawExpression)
        assertEquals(
            0.0,
            subject.evaluate(expression, mapOf("x" to 0.0, "y" to 1.2))
        )

        assertEquals(
            156.3 * 1.2,
            subject.evaluate(expression, mapOf("x" to 156.3, "y" to 1.2))
        )
    }
}