package com.murzagalin.evaluator.tokenizer

import com.murzagalin.evaluator.DefaultFunctions
import com.murzagalin.evaluator.Token
import com.murzagalin.evaluator.Tokenizer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FunctionsTest {

    private val subject = Tokenizer()

    @Test
    fun simple_functions_with_numbers() {
        DefaultFunctions.ALL.filter { it.argsCount.first == 1 }.forEach { f ->
            assertContentEquals(
                listOf(
                    Token.FunctionCall(1, f),
                    Token.Bracket.Left,
                    Token.Operand.Number(1),
                    Token.Bracket.Right
                ),
                subject.tokenize("${f.name}(1.0)")
            )
        }
    }

    @Test
    fun simple_functions_with_2_numbers() {
        DefaultFunctions.ALL.filter { it.argsCount.first == 2 }.forEach { f ->
            assertContentEquals(
                listOf(
                    Token.FunctionCall(2, f),
                    Token.Bracket.Left,
                    Token.Operand.Number(1),
                    Token.FunctionCall.Delimiter,
                    Token.Operand.Number(2.2),
                    Token.Bracket.Right
                ),
                subject.tokenize("${f.name}(1.0, 2.2)")
            )
        }
    }

    @Test
    fun expressions_with_functions() {
        assertContentEquals(
            listOf(
                Token.Operand.Number(1.0),
                Token.Operator.Plus,
                Token.FunctionCall(1, DefaultFunctions.COS),
                Token.Bracket.Left,
                Token.Operand.Number(1),
                Token.Bracket.Right,
                Token.Operator.Multiplication,
                Token.Operand.Number(5.0)
            ),
            subject.tokenize("1+cos(1.0)*5.0")
        )
        assertContentEquals(
            listOf(
                Token.Bracket.Left,
                Token.Operand.Number(1.0),
                Token.Operator.Division,
                Token.FunctionCall(1, DefaultFunctions.COS),
                Token.Bracket.Left,
                Token.Operand.Number(1.0),
                Token.Bracket.Right,
                Token.Bracket.Right,
                Token.Operator.Power,
                Token.Operand.Number(4.2),
                Token.Operator.Multiplication,
                Token.Operand.Number(5.0)
            ),
            subject.tokenize("(1/cos(1.0))^4.2*5.0")
        )
    }

    @Test
    fun function_in_a_function() {
        assertContentEquals(
            listOf(
                Token.FunctionCall(1, DefaultFunctions.COS),
                Token.Bracket.Left,
                Token.FunctionCall(1, DefaultFunctions.LN),
                Token.Bracket.Left,
                Token.Operand.Number(10.0),
                Token.Bracket.Right,
                Token.Bracket.Right
            ),
            subject.tokenize("cos(ln(10))")
        )
    }

    @Test
    fun function_in_a_function_with_operators() {
        assertContentEquals(
            listOf(
                Token.FunctionCall(1, DefaultFunctions.COS),
                Token.Bracket.Left,
                Token.Operand.Number(2.0),
                Token.Operator.Multiplication,
                Token.FunctionCall(1, DefaultFunctions.LN),
                Token.Bracket.Left,
                Token.Operand.Number(10.0),
                Token.Bracket.Right,
                Token.Operator.Plus,
                Token.Operand.Number(1.0),
                Token.Bracket.Right
            ),
            subject.tokenize("cos(2*ln(10)+1)")
        )
    }


    @Test
    fun non_recognized_function() {
        val nonExistingFunctionName = "fiction_function"
        assertFailsWith<IllegalArgumentException>() {
            subject.tokenize("$nonExistingFunctionName(1.0)")
        }
    }

    @Test
    fun wong_number_of_arguments() {
        assertFailsWith<IllegalArgumentException>() {
            subject.tokenize("log(2.0)")
        }
        assertFailsWith<IllegalArgumentException>() {
            subject.tokenize("cos(1.0, 2.0)")
        }
        assertFailsWith<IllegalArgumentException>() {
            subject.tokenize("min()")
        }
    }

    @Test
    fun correct_number_of_arguments() {
        assertEquals(
            3,
            (subject.tokenize("min(1, 2, 3)")[0] as Token.FunctionCall).argsCount
        )
        assertEquals(
            1,
            (subject.tokenize("cos(1)")[0] as Token.FunctionCall).argsCount
        )
        assertEquals(
            1,
            (subject.tokenize("cos(sin(cos(sin(1))))")[0] as Token.FunctionCall).argsCount
        )
        assertEquals(
            3,
            (subject.tokenize("min(sin(1), max(2, 4, 6, 7), ln(3))")[0] as Token.FunctionCall).argsCount
        )
        assertEquals(
            4,
            (subject.tokenize("max(2, 4, 6, 7)")[0] as Token.FunctionCall).argsCount
        )
    }
}