package com.murzagalin.evaluator.converter

import com.murzagalin.evaluator.Converter
import com.murzagalin.evaluator.DefaultFunctions
import com.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertContentEquals

class FunctionTest {

    private val subject = Converter()

    @Test
    fun simple_function_call() {
        val expression = listOf(
            Token.FunctionCall(1, DefaultFunctions.COS),
            Token.Bracket.Left,
            Token.Operand.Num(10.0),
            Token.Bracket.Right
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(10.0),
                Token.FunctionCall(1, DefaultFunctions.COS),
            ),
            result.expression
        )
    }

    @Test
    fun sum_and_function_call() {
        val expression = listOf(
            Token.Operand.Num(1.0),
            Token.Operator.Sum,
            Token.FunctionCall(1, DefaultFunctions.COS),
            Token.Bracket.Left,
            Token.Operand.Num(10.0),
            Token.Bracket.Right
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(1.0),
                Token.Operand.Num(10.0),
                Token.FunctionCall(1, DefaultFunctions.COS),
                Token.Operator.Sum
            ),
            result.expression
        )
    }

    @Test
    fun function_in_a_function() {
        val expression = listOf(
            Token.FunctionCall(1, DefaultFunctions.COS),
            Token.Bracket.Left,
            Token.FunctionCall(1, DefaultFunctions.LN),
            Token.Bracket.Left,
            Token.Operand.Num(10.0),
            Token.Bracket.Right,
            Token.Bracket.Right
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.Num(10.0),
                Token.FunctionCall(1, DefaultFunctions.LN),
                Token.FunctionCall(1, DefaultFunctions.COS),
            ),
            result.expression
        )
    }

    @Test
    fun function_with_two_params() {
        val expression = listOf(
            Token.FunctionCall(2, DefaultFunctions.LOG),
            Token.Bracket.Left,
            Token.Operand.Num(10.0),
            Token.FunctionCall.Delimiter,
            Token.Operand.Num(12.0),
            Token.Bracket.Right
        )

        assertContentEquals(
            listOf(
                Token.Operand.Num(10.0),
                Token.Operand.Num(12.0),
                Token.FunctionCall(2, DefaultFunctions.LOG)
            ),
            subject.convert(expression).expression
        )
    }

    @Test
    fun multi_var_function_with_calculated_first_param() {
        val expression = listOf(
            Token.FunctionCall(2, DefaultFunctions.LOG),
            Token.Bracket.Left,
            Token.Operand.Num(2),
            Token.Operator.Mult,
            Token.Operand.Num(2),
            Token.FunctionCall.Delimiter,
            Token.Operand.Num(3),
            Token.Bracket.Right
        )
        assertContentEquals(
            listOf(
                Token.Operand.Num(2),
                Token.Operand.Num(2),
                Token.Operator.Mult,
                Token.Operand.Num(3),
                Token.FunctionCall(2, DefaultFunctions.LOG)
            ),
            subject.convert(expression).expression
        )
    }
}