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
            Token.Operand.Num(4),
            Token.Operator.Sum,
            Token.Operand.Num(3)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(4),
                Token.Operand.Num(3),
                Token.Operator.Sum
            ),
            result
        )
    }

    @Test
    fun convert_mult() {
        val expression = listOf(
            Token.Operand.Num(5),
            Token.Operator.Mult,
            Token.Operand.Num(6)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(5),
                Token.Operand.Num(6),
                Token.Operator.Mult
            ),
            result
        )
    }

    @Test
    fun convert_sub() {
        val expression = listOf(
            Token.Operand.Num(3),
            Token.Operator.Sub,
            Token.Operand.Num(1)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(3),
                Token.Operand.Num(1),
                Token.Operator.Sub
            ),
            result
        )
    }

    @Test
    fun convert_div() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operator.Div,
            Token.Operand.Num(6)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(8),
                Token.Operand.Num(6),
                Token.Operator.Div
            ),
            result
        )
    }

    @Test
    fun convert_sum_mult() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operator.Sum,
            Token.Operand.Num(6),
            Token.Operator.Mult,
            Token.Operand.Num(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(8),
                Token.Operand.Num(6),
                Token.Operand.Num(4),
                Token.Operator.Mult,
                Token.Operator.Sum,
            ),
            result
        )
    }

    @Test
    fun convert_sum_modulo() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operator.Sum,
            Token.Operand.Num(6),
            Token.Operator.Mod,
            Token.Operand.Num(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(8),
                Token.Operand.Num(6),
                Token.Operand.Num(4),
                Token.Operator.Mod,
                Token.Operator.Sum,
            ),
            result
        )
    }

    @Test
    fun convert_mult_sum() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operator.Mult,
            Token.Operand.Num(6),
            Token.Operator.Sum,
            Token.Operand.Num(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(8),
                Token.Operand.Num(6),
                Token.Operator.Mult,
                Token.Operand.Num(4),
                Token.Operator.Sum,
            ),
            result
        )
    }

    @Test
    fun convert_sub_mult() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operator.Sub,
            Token.Operand.Num(6),
            Token.Operator.Mult,
            Token.Operand.Num(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(8),
                Token.Operand.Num(6),
                Token.Operand.Num(4),
                Token.Operator.Mult,
                Token.Operator.Sub,
            ),
            result
        )
    }

    @Test
    fun convert_div_mult() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operator.Div,
            Token.Operand.Num(6),
            Token.Operator.Mult,
            Token.Operand.Num(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(8),
                Token.Operand.Num(6),
                Token.Operator.Div,
                Token.Operand.Num(4),
                Token.Operator.Mult,
            ),
            result
        )
    }

    @Test
    fun convert_div_pow() {
        val expression = listOf(
            Token.Operand.Num(8),
            Token.Operator.Div,
            Token.Operand.Num(6),
            Token.Operator.Pow,
            Token.Operand.Num(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(8),
                Token.Operand.Num(6),
                Token.Operand.Num(4),
                Token.Operator.Pow,
                Token.Operator.Div
            ),
            result
        )
    }

    @Test
    fun sum_in_brackets_multiplied() {
        val expression = listOf(
            Token.Bracket.Left,
            Token.Operand.Num(321),
            Token.Operator.Sum,
            Token.Operand.Num(749),
            Token.Bracket.Right,
            Token.Operator.Mult,
            Token.Operand.Num(9)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(321),
                Token.Operand.Num(749),
                Token.Operator.Sum,
                Token.Operand.Num(9),
                Token.Operator.Mult
            ),
            result
        )
    }

    @Test
    fun sum_in_brackets_without_left_bracket_multiplied() {
        val expression = listOf(
            Token.Operand.Num(321),
            Token.Operator.Sum,
            Token.Operand.Num(749),
            Token.Bracket.Right,
            Token.Operator.Mult,
            Token.Operand.Num(9)
        )
        assertFailsWith(IllegalArgumentException::class, "mismatched parenthesis") {
            subject.convert(expression)
        }
    }

    @Test
    fun sum_in_brackets_without_right_bracket_multiplied() {
        val expression = listOf(
            Token.Bracket.Left,
            Token.Operand.Num(321),
            Token.Operator.Sum,
            Token.Operand.Num(749),
            Token.Operator.Mult,
            Token.Operand.Num(9)
        )
        assertFailsWith(IllegalArgumentException::class, "mismatched parenthesis") {
            subject.convert(expression)
        }
    }

    @Test
    fun div_in_brackets_powered() {
        val expression = listOf(
            Token.Bracket.Left,
            Token.Operand.Num(321),
            Token.Operator.Div,
            Token.Operand.Num(749),
            Token.Bracket.Right,
            Token.Operator.Pow,
            Token.Operand.Num(9)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(321),
                Token.Operand.Num(749),
                Token.Operator.Div,
                Token.Operand.Num(9),
                Token.Operator.Pow
            ),
            result
        )
    }
}