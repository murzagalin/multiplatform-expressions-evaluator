package com.github.murzagalin.evaluator.tokenizer

import com.github.murzagalin.evaluator.Token
import com.github.murzagalin.evaluator.Tokenizer
import kotlin.test.Test
import kotlin.test.assertContentEquals

class UnaryOperatorsTokenizerTests {

    private val subject = Tokenizer()

    @Test
    fun unary_minus_with_number() {
        val expression = "-5"
        val result = subject.tokenize(expression)
        assertContentEquals(
            listOf(
                Token.Operator.UnaryMinus,
                Token.Operand.Number(5)
            ),
            result
        )
    }

    @Test
    fun unary_plus_with_number() {
        val expression = "+5"
        val result = subject.tokenize(expression)
        assertContentEquals(
            listOf(
                Token.Operator.UnaryPlus,
                Token.Operand.Number(5)
            ),
            result
        )
    }

    @Test
    fun sum_with_unary_minus() {
        val expression = "78+-5"
        val result = subject.tokenize(expression)
        assertContentEquals(
            listOf(
                Token.Operand.Number(78),
                Token.Operator.Plus,
                Token.Operator.UnaryMinus,
                Token.Operand.Number(5)
            ),
            result
        )
    }

    @Test
    fun sum_with_unary_plus() {
        val expression = "78++5"
        val result = subject.tokenize(expression)
        assertContentEquals(
            listOf(
                Token.Operand.Number(78),
                Token.Operator.Plus,
                Token.Operator.UnaryPlus,
                Token.Operand.Number(5)
            ),
            result
        )
    }

    @Test
    fun brackets_with_unary_minus() {
        val expression = "-(1+ 2)"
        val result = subject.tokenize(expression)
        assertContentEquals(
            listOf(
                Token.Operator.UnaryMinus,
                Token.Bracket.Left,
                Token.Operand.Number(1),
                Token.Operator.Plus,
                Token.Operand.Number(2),
                Token.Bracket.Right
            ),
            result
        )
    }

    @Test
    fun mult_brackets_with_unary_minus() {
        val expression = "10*-(1+ 2)"
        val result = subject.tokenize(expression)
        assertContentEquals(
            listOf(
                Token.Operand.Number(10),
                Token.Operator.Multiplication,
                Token.Operator.UnaryMinus,
                Token.Bracket.Left,
                Token.Operand.Number(1),
                Token.Operator.Plus,
                Token.Operand.Number(2),
                Token.Bracket.Right
            ),
            result
        )
    }

    @Test
    fun mult_brackets_with_unary_plus() {
        val expression = "10*+(1+ 2)"
        val result = subject.tokenize(expression)
        assertContentEquals(
            listOf(
                Token.Operand.Number(10),
                Token.Operator.Multiplication,
                Token.Operator.UnaryPlus,
                Token.Bracket.Left,
                Token.Operand.Number(1),
                Token.Operator.Plus,
                Token.Operand.Number(2),
                Token.Bracket.Right
            ),
            result
        )
    }

    @Test
    fun power_with_unary_minus() {
        val expression = "4^-2"
        val result = subject.tokenize(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(4),
                Token.Operator.Power,
                Token.Operator.UnaryMinus,
                Token.Operand.Number(2)
            ),
            result
        )
    }

    @Test
    fun power_with_unary_plus() {
        val expression = "4^+2"
        val result = subject.tokenize(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Number(4),
                Token.Operator.Power,
                Token.Operator.UnaryPlus,
                Token.Operand.Number(2)
            ),
            result
        )
    }
}