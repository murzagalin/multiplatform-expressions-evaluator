package com.murzagalin.evaluator.tokenizer

import com.murzagalin.evaluator.DefaultFunctions
import com.murzagalin.evaluator.Token
import com.murzagalin.evaluator.Tokenizer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

class FunctionsTest {

    private val subject = Tokenizer()

    @Test
    fun simple_functions_with_numbers() {
        DefaultFunctions.ALL.filter { it.argsCount.first == 1 && it.argsCount.last == 1 }.forEach { f ->
            assertContentEquals(
                listOf(
                    Token.FunctionCall(1, f),
                    Token.Bracket.Left,
                    Token.Operand.Num(1),
                    Token.Bracket.Right
                ),
                subject.tokenize("${f.name}(1.0)")
            )
        }
    }

    @Test
    fun simple_functions_with_2_numbers() {
        DefaultFunctions.ALL.filter { it.argsCount.first == 2 && it.argsCount.last == 2 }.forEach { f ->
            assertContentEquals(
                listOf(
                    Token.FunctionCall(2, f),
                    Token.Bracket.Left,
                    Token.Operand.Num(1),
                    Token.FunctionCall.Delimiter,
                    Token.Operand.Num(2.2),
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
                Token.Operand.Num(1.0),
                Token.Operator.Sum,
                Token.FunctionCall(1, DefaultFunctions.COS),
                Token.Bracket.Left,
                Token.Operand.Num(1),
                Token.Bracket.Right,
                Token.Operator.Mult,
                Token.Operand.Num(5.0)
            ),
            subject.tokenize("1+cos(1.0)*5.0")
        )
        assertContentEquals(
            listOf(
                Token.Bracket.Left,
                Token.Operand.Num(1.0),
                Token.Operator.Div,
                Token.FunctionCall(1, DefaultFunctions.COS),
                Token.Bracket.Left,
                Token.Operand.Num(1.0),
                Token.Bracket.Right,
                Token.Bracket.Right,
                Token.Operator.Pow,
                Token.Operand.Num(4.2),
                Token.Operator.Mult,
                Token.Operand.Num(5.0)
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
                Token.Operand.Num(10.0),
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
                Token.Operand.Num(2.0),
                Token.Operator.Mult,
                Token.FunctionCall(1, DefaultFunctions.LN),
                Token.Bracket.Left,
                Token.Operand.Num(10.0),
                Token.Bracket.Right,
                Token.Operator.Sum,
                Token.Operand.Num(1.0),
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
}