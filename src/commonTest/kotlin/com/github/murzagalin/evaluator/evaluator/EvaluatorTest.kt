package com.github.murzagalin.evaluator.evaluator

import com.github.murzagalin.evaluator.DoubleEvaluator
import com.github.murzagalin.evaluator.PreprocessedExpression
import com.github.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertEquals

class EvaluatorTest {

    private val subject = DoubleEvaluator()

    @Test
    fun simple_sum() {
        val expression = PreprocessedExpression(
            listOf(
                Token.Operand.Number(4),
                Token.Operand.Number(3),
                Token.Operator.Plus
            )
        )
        val result = subject.evaluate(expression)
        assertEquals(7.0, result)
    }

    @Test
    fun simple_mult() {
        val expression = PreprocessedExpression(
            listOf(
                Token.Operand.Number(4),
                Token.Operand.Number(3),
                Token.Operator.Multiplication
            )
        )
        val result = subject.evaluate(expression)
        assertEquals(12.0, result)
    }

    @Test
    fun simple_div() {
        val expression = PreprocessedExpression(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(2),
                Token.Operator.Division
            )
        )
        val result = subject.evaluate(expression)
        assertEquals(4.0, result)
    }

    @Test
    fun simple_sub() {
        val expression = PreprocessedExpression(
            listOf(
                Token.Operand.Number(4),
                Token.Operand.Number(3),
                Token.Operator.Minus
            )
        )
        val result = subject.evaluate(expression)
        assertEquals(1.0, result)
    }

    @Test
    fun simple_pow() {
        val expression = PreprocessedExpression(
            listOf(
                Token.Operand.Number(4),
                Token.Operand.Number(3),
                Token.Operator.Power
            )
        )
        val result = subject.evaluate(expression)
        assertEquals(64.0, result)
    }

    @Test
    fun mult_sum() {
        val expression = PreprocessedExpression(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(6),
                Token.Operator.Multiplication,
                Token.Operand.Number(4),
                Token.Operator.Plus
            )
        )
        val result = subject.evaluate(expression)
        assertEquals(52.0, result)
    }

    @Test
    fun sum_mult() {
        val expression = PreprocessedExpression(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(6),
                Token.Operand.Number(4),
                Token.Operator.Multiplication,
                Token.Operator.Plus,
            )
        )
        val result = subject.evaluate(expression)
        assertEquals(32.0, result)
    }

    @Test
    fun div_pow() {
        val expression = PreprocessedExpression(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(2),
                Token.Operand.Number(2),
                Token.Operator.Power,
                Token.Operator.Division
            )
        )
        val result = subject.evaluate(expression)
        assertEquals(2.0, result)
    }

    @Test
    fun div_mult() {
        val expression = PreprocessedExpression(
            listOf(
                Token.Operand.Number(9),
                Token.Operand.Number(3),
                Token.Operator.Division,
                Token.Operand.Number(4),
                Token.Operator.Multiplication,
            )
        )
        val result = subject.evaluate(expression)
        assertEquals(12.0, result)
    }

    @Test
    fun modulo() {
        assertEquals(
            1.0,
            subject.evaluate(
                PreprocessedExpression(
                    listOf(Token.Operand.Number(9), Token.Operand.Number(4), Token.Operator.Modulo)
                )
            )
        )
        assertEquals(
            0.0,
            subject.evaluate(
                PreprocessedExpression(
                    listOf(Token.Operand.Number(9), Token.Operand.Number(3), Token.Operator.Modulo)
                )
            )
        )
        assertEquals(
            2.0,
            subject.evaluate(
                PreprocessedExpression(
                    listOf(Token.Operand.Number(2), Token.Operand.Number(5), Token.Operator.Modulo)
                )
            )
        )
    }
}