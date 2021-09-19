package tokenizer

import Tokenizer
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
                Token.Operand.Num(5)
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
                Token.Operand.Num(5)
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
                Token.Operand.Num(78),
                Token.Operator.Sum,
                Token.Operator.UnaryMinus,
                Token.Operand.Num(5)
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
                Token.Operand.Num(78),
                Token.Operator.Sum,
                Token.Operator.UnaryPlus,
                Token.Operand.Num(5)
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
                Token.Operand.Num(1),
                Token.Operator.Sum,
                Token.Operand.Num(2),
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
                Token.Operand.Num(10),
                Token.Operator.Mult,
                Token.Operator.UnaryMinus,
                Token.Bracket.Left,
                Token.Operand.Num(1),
                Token.Operator.Sum,
                Token.Operand.Num(2),
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
                Token.Operand.Num(10),
                Token.Operator.Mult,
                Token.Operator.UnaryPlus,
                Token.Bracket.Left,
                Token.Operand.Num(1),
                Token.Operator.Sum,
                Token.Operand.Num(2),
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
                Token.Operand.Num(4),
                Token.Operator.Pow,
                Token.Operator.UnaryMinus,
                Token.Operand.Num(2)
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
                Token.Operand.Num(4),
                Token.Operator.Pow,
                Token.Operator.UnaryPlus,
                Token.Operand.Num(2)
            ),
            result
        )
    }
}