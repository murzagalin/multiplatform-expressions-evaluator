package com.murzagalin.evaluator.converter

import com.murzagalin.evaluator.Converter
import com.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

class ConverterTest {

    private val subject = Converter()

    @Test
    fun convert_sum() {
        val expression = listOf(
            Token.Operand.Number(4),
            Token.Operator.Plus,
            Token.Operand.Number(3)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(4),
                Token.Operand.Number(3),
                Token.Operator.Plus
            ),
            result.expression
        )
    }

    @Test
    fun convert_mult() {
        val expression = listOf(
            Token.Operand.Number(5),
            Token.Operator.Multiplication,
            Token.Operand.Number(6)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(5),
                Token.Operand.Number(6),
                Token.Operator.Multiplication
            ),
            result.expression
        )
    }

    @Test
    fun convert_sub() {
        val expression = listOf(
            Token.Operand.Number(3),
            Token.Operator.Minus,
            Token.Operand.Number(1)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(3),
                Token.Operand.Number(1),
                Token.Operator.Minus
            ),
            result.expression
        )
    }

    @Test
    fun convert_div() {
        val expression = listOf(
            Token.Operand.Number(8),
            Token.Operator.Division,
            Token.Operand.Number(6)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(6),
                Token.Operator.Division
            ),
            result.expression
        )
    }

    @Test
    fun convert_sum_mult() {
        val expression = listOf(
            Token.Operand.Number(8),
            Token.Operator.Plus,
            Token.Operand.Number(6),
            Token.Operator.Multiplication,
            Token.Operand.Number(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(6),
                Token.Operand.Number(4),
                Token.Operator.Multiplication,
                Token.Operator.Plus,
            ),
            result.expression
        )
    }

    @Test
    fun convert_sum_modulo() {
        val expression = listOf(
            Token.Operand.Number(8),
            Token.Operator.Plus,
            Token.Operand.Number(6),
            Token.Operator.Modulo,
            Token.Operand.Number(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(6),
                Token.Operand.Number(4),
                Token.Operator.Modulo,
                Token.Operator.Plus,
            ),
            result.expression
        )
    }

    @Test
    fun convert_mult_sum() {
        val expression = listOf(
            Token.Operand.Number(8),
            Token.Operator.Multiplication,
            Token.Operand.Number(6),
            Token.Operator.Plus,
            Token.Operand.Number(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(6),
                Token.Operator.Multiplication,
                Token.Operand.Number(4),
                Token.Operator.Plus,
            ),
            result.expression
        )
    }

    @Test
    fun convert_sub_mult() {
        val expression = listOf(
            Token.Operand.Number(8),
            Token.Operator.Minus,
            Token.Operand.Number(6),
            Token.Operator.Multiplication,
            Token.Operand.Number(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(6),
                Token.Operand.Number(4),
                Token.Operator.Multiplication,
                Token.Operator.Minus,
            ),
            result.expression
        )
    }

    @Test
    fun convert_div_mult() {
        val expression = listOf(
            Token.Operand.Number(8),
            Token.Operator.Division,
            Token.Operand.Number(6),
            Token.Operator.Multiplication,
            Token.Operand.Number(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(6),
                Token.Operator.Division,
                Token.Operand.Number(4),
                Token.Operator.Multiplication,
            ),
            result.expression
        )
    }

    @Test
    fun convert_div_pow() {
        val expression = listOf(
            Token.Operand.Number(8),
            Token.Operator.Division,
            Token.Operand.Number(6),
            Token.Operator.Power,
            Token.Operand.Number(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(8),
                Token.Operand.Number(6),
                Token.Operand.Number(4),
                Token.Operator.Power,
                Token.Operator.Division
            ),
            result.expression
        )
    }

    @Test
    fun sum_in_brackets_multiplied() {
        val expression = listOf(
            Token.Bracket.Left,
            Token.Operand.Number(321),
            Token.Operator.Plus,
            Token.Operand.Number(749),
            Token.Bracket.Right,
            Token.Operator.Multiplication,
            Token.Operand.Number(9)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(321),
                Token.Operand.Number(749),
                Token.Operator.Plus,
                Token.Operand.Number(9),
                Token.Operator.Multiplication
            ),
            result.expression
        )
    }

    @Test
    fun sum_in_brackets_without_left_bracket_multiplied() {
        val expression = listOf(
            Token.Operand.Number(321),
            Token.Operator.Plus,
            Token.Operand.Number(749),
            Token.Bracket.Right,
            Token.Operator.Multiplication,
            Token.Operand.Number(9)
        )
        assertFailsWith(IllegalArgumentException::class, "mismatched parenthesis") {
            subject.convert(expression)
        }
    }

    @Test
    fun sum_in_brackets_without_right_bracket_multiplied() {
        val expression = listOf(
            Token.Bracket.Left,
            Token.Operand.Number(321),
            Token.Operator.Plus,
            Token.Operand.Number(749),
            Token.Operator.Multiplication,
            Token.Operand.Number(9)
        )
        assertFailsWith(IllegalArgumentException::class, "mismatched parenthesis") {
            subject.convert(expression)
        }
    }

    @Test
    fun div_in_brackets_powered() {
        val expression = listOf(
            Token.Bracket.Left,
            Token.Operand.Number(321),
            Token.Operator.Division,
            Token.Operand.Number(749),
            Token.Bracket.Right,
            Token.Operator.Power,
            Token.Operand.Number(9)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(321),
                Token.Operand.Number(749),
                Token.Operator.Division,
                Token.Operand.Number(9),
                Token.Operator.Power
            ),
            result.expression
        )
    }
}