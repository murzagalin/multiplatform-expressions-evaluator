package com.murzagalin.evaluator.evaluator

import com.murzagalin.evaluator.DoubleEvaluator
import com.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertEquals

class EvaluatorTest {

    private val subject = DoubleEvaluator()

    @Test
    fun simple_sum() {
        val expression = listOf(
            Token.Operand.Num(4),
            Token.Operand.Num(3),
            Token.Operator.Sum
        )
        val result = subject.evaluate(expression)
        assertEquals(7.0, result)
    }

    @Test
    fun simple_mult() {
        val expression = listOf(
            Token.Operand.Num(4),
            Token.Operand.Num(3),
            Token.Operator.Mult
        )
        val result = subject.evaluate(expression)
        assertEquals(12.0, result)
    }

    @Test
    fun simple_div() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operand.Num(2),
            Token.Operator.Div
        )
        val result = subject.evaluate(expression)
        assertEquals(4.0, result)
    }

    @Test
    fun simple_sub() {
        val expression = listOf(
            Token.Operand.Num(4),
            Token.Operand.Num(3),
            Token.Operator.Sub
        )
        val result = subject.evaluate(expression)
        assertEquals(1.0, result)
    }

    @Test
    fun simple_pow() {
        val expression = listOf(
            Token.Operand.Num(4),
            Token.Operand.Num(3),
            Token.Operator.Pow
        )
        val result = subject.evaluate(expression)
        assertEquals(64.0, result)
    }

    @Test
    fun mult_sum() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operand.Num(6),
            Token.Operator.Mult,
            Token.Operand.Num(4),
            Token.Operator.Sum
        )
        val result = subject.evaluate(expression)
        assertEquals(52.0, result)
    }

    @Test
    fun sum_mult() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operand.Num(6),
            Token.Operand.Num(4),
            Token.Operator.Mult,
            Token.Operator.Sum,
        )
        val result = subject.evaluate(expression)
        assertEquals(32.0, result)
    }

    @Test
    fun div_pow() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operand.Num(2),
            Token.Operand.Num(2),
            Token.Operator.Pow,
            Token.Operator.Div
        )
        val result = subject.evaluate(expression)
        assertEquals(2.0, result)
    }

    @Test
    fun div_mult() {
        val expression = listOf(
            Token.Operand.Num(9),
            Token.Operand.Num(3),
            Token.Operator.Div,
            Token.Operand.Num(4),
            Token.Operator.Mult,
        )
        val result = subject.evaluate(expression)
        assertEquals(12.0, result)
    }

    @Test
    fun modulo() {
        assertEquals(
            1.0,
            subject.evaluate(
                listOf(Token.Operand.Num(9), Token.Operand.Num(4), Token.Operator.Mod)
            )
        )
        assertEquals(
            0.0,
            subject.evaluate(
                listOf(Token.Operand.Num(9), Token.Operand.Num(3), Token.Operator.Mod)
            )
        )
        assertEquals(
            2.0,
            subject.evaluate(
                listOf(Token.Operand.Num(2), Token.Operand.Num(5), Token.Operator.Mod)
            )
        )
    }
}