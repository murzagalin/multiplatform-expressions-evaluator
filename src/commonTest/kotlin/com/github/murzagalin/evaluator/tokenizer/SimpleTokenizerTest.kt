package com.github.murzagalin.evaluator.tokenizer

import com.github.murzagalin.evaluator.Token
import com.github.murzagalin.evaluator.Tokenizer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class SimpleTokenizerTest {

    private val subject = Tokenizer()

    @Test
    fun parsing_int_value() {
        val result = subject.tokenize("12345")

        assertContentEquals(
            listOf(Token.Operand.Number(12345)),
            result
        )
    }

    @Test
    fun sum_of_2_simple_operands() {
        val result = subject.tokenize("3+4")
        assertEquals(3, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.Number(3),
                Token.Operator.Plus,
                Token.Operand.Number(4)
            ),
            result
        )
    }


    @Test
    fun plus_an_minus_after_brackets() {
        assertContentEquals(
            listOf(
                Token.Bracket.Left,
                Token.Operand.Number(12),
                Token.Operator.Plus,
                Token.Operand.Number(32),
                Token.Bracket.Right,
                Token.Operator.Minus,
                Token.Operand.Number(3)
            ),
            subject.tokenize("(12+32)-3")
        )
        assertContentEquals(
            listOf(
                Token.Bracket.Left,
                Token.Operand.Number(12),
                Token.Operator.Plus,
                Token.Operand.Number(32),
                Token.Bracket.Right,
                Token.Operator.Plus,
                Token.Operand.Number(3)
            ),
            subject.tokenize("(12+32)+3")
        )
    }

    @Test
    fun sub_of_2_operands() {
        val result = subject.tokenize("9683-2365")
        assertEquals(3, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.Number(9683),
                Token.Operator.Minus,
                Token.Operand.Number(2365)
            ),
            result
        )
    }

    @Test
    fun pow_of_2_simple_operands() {
        val result = subject.tokenize("33^4")
        assertEquals(3, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.Number(33),
                Token.Operator.Power,
                Token.Operand.Number(4)
            ),
            result
        )
    }

    @Test
    fun division_of_2_operands() {
        val result = subject.tokenize("2536/575")
        assertEquals(3, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.Number(2536),
                Token.Operator.Division,
                Token.Operand.Number(575)
            ),
            result
        )
    }

    @Test
    fun sum_of_2_simple_operands_with_spaces() {
        val result = subject.tokenize(" 3 + 4 ")
        assertEquals(3, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.Number(3),
                Token.Operator.Plus,
                Token.Operand.Number(4)
            ),
            result
        )
    }

    @Test
    fun multiplication_of_2_operands() {
        val result = subject.tokenize("5325*289")
        assertEquals(3, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.Number(5325),
                Token.Operator.Multiplication,
                Token.Operand.Number(289)
            ),
            result
        )
    }

    @Test
    fun sum_and_multiplication_with_3_simple_operands() {
        val result = subject.tokenize("1 + 8*9")
        assertEquals(5, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.Number(1),
                Token.Operator.Plus,
                Token.Operand.Number(8),
                Token.Operator.Multiplication,
                Token.Operand.Number(9)
            ),
            result
        )
    }

    @Test
    fun sum_and_multiplication_with_3_operands() {
        val result = subject.tokenize("1432 + 8585*9346")
        assertEquals(5, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.Number(1432),
                Token.Operator.Plus,
                Token.Operand.Number(8585),
                Token.Operator.Multiplication,
                Token.Operand.Number(9346)
            ),
            result
        )
    }

    @Test
    fun expression_with_brackets() {
        val result = subject.tokenize("(1432 + 8585)*9346")
        assertEquals(7, result.size)
        assertContentEquals(
            listOf(
                Token.Bracket.Left,
                Token.Operand.Number(1432),
                Token.Operator.Plus,
                Token.Operand.Number(8585),
                Token.Bracket.Right,
                Token.Operator.Multiplication,
                Token.Operand.Number(9346)
            ),
            result
        )
    }

    @Test
    fun modulo() {
        val result = subject.tokenize("2.5%0.26")
        assertEquals(3, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.Number(2.5),
                Token.Operator.Modulo,
                Token.Operand.Number(0.26)
            ),
            result
        )
    }
}