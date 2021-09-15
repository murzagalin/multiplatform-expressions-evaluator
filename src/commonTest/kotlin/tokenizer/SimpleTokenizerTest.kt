package tokenizer

import Token
import Tokenizer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class SimpleTokenizerTest {

    private val subject = Tokenizer()

    @Test
    fun parsing_int_value() {
        val result = subject.tokenize("12345")

        assertContentEquals(
            listOf(Token.Operand(12345)),
            result
        )
    }

    @Test
    fun sum_of_2_simple_operands() {
        val result = subject.tokenize("3+4")
        assertEquals(3, result.size)
        assertContentEquals(
            listOf(
                Token.Operand(3),
                Token.Operator.Sum,
                Token.Operand(4)
            ),
            result
        )
    }

    @Test
    fun sub_of_2_operands() {
        val result = subject.tokenize("9683-2365")
        assertEquals(3, result.size)
        assertContentEquals(
            listOf(
                Token.Operand(9683),
                Token.Operator.Sub,
                Token.Operand(2365)
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
                Token.Operand(33),
                Token.Operator.Pow,
                Token.Operand(4)
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
                Token.Operand(2536),
                Token.Operator.Div,
                Token.Operand(575)
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
                Token.Operand(3),
                Token.Operator.Sum,
                Token.Operand(4)
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
                Token.Operand(5325),
                Token.Operator.Mult,
                Token.Operand(289)
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
                Token.Operand(1),
                Token.Operator.Sum,
                Token.Operand(8),
                Token.Operator.Mult,
                Token.Operand(9)
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
                Token.Operand(1432),
                Token.Operator.Sum,
                Token.Operand(8585),
                Token.Operator.Mult,
                Token.Operand(9346)
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
                Token.Operand(1432),
                Token.Operator.Sum,
                Token.Operand(8585),
                Token.Bracket.Right,
                Token.Operator.Mult,
                Token.Operand(9346)
            ),
            result
        )
    }
}