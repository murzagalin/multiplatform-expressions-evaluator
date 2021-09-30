package converter

import COS
import Converter
import LN
import LOG
import kotlin.test.Test
import kotlin.test.assertContentEquals

class FunctionTest {

    private val subject = Converter()

    @Test
    fun simple_function_call() {
        val expression = listOf(
            COS,
            Token.Bracket.Left,
            Token.Operand.Num(10.0),
            Token.Bracket.Right
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(10.0),
                COS
            ),
            result
        )
    }

    @Test
    fun sum_and_function_call() {
        val expression = listOf(
            Token.Operand.Num(1.0),
            Token.Operator.Sum,
            COS,
            Token.Bracket.Left,
            Token.Operand.Num(10.0),
            Token.Bracket.Right
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(1.0),
                Token.Operand.Num(10.0),
                COS,
                Token.Operator.Sum
            ),
            result
        )
    }

    @Test
    fun function_in_a_function() {
        val expression = listOf(
            COS,
            Token.Bracket.Left,
            LN,
            Token.Bracket.Left,
            Token.Operand.Num(10.0),
            Token.Bracket.Right,
            Token.Bracket.Right
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(10.0),
                LN,
                COS
            ),
            result
        )
    }

    @Test
    fun function_with_two_params() {
        val expression = listOf(
            LOG,
            Token.Bracket.Left,
            Token.Operand.Num(10.0),
            Token.Function.Delimiter,
            Token.Operand.Num(12.0),
            Token.Bracket.Right
        )

        assertContentEquals(
            listOf(
                Token.Operand.Num(10.0),
                Token.Operand.Num(12.0),
                LOG
            ),
            subject.convert(expression)
        )
    }

    @Test
    fun multi_var_function_with_calculated_first_param() {
        val expression = listOf(
            LOG,
            Token.Bracket.Left,
            Token.Operand.Num(2),
            Token.Operator.Mult,
            Token.Operand.Num(2),
            Token.Function.Delimiter,
            Token.Operand.Num(3),
            Token.Bracket.Right
        )
        assertContentEquals(
            listOf(
                Token.Operand.Num(2),
                Token.Operand.Num(2),
                Token.Operator.Mult,
                Token.Operand.Num(3),
                LOG
            ),
            subject.convert(expression)
        )
    }
}