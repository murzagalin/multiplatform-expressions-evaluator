package com.github.murzagalin.evaluator.ast

import com.github.murzagalin.evaluator.DefaultFunctions
import com.github.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserFunctionsTest {

    private val subject = Parser()

    @Test
    fun parse_function_call_without_arguments() {
        val functionCall = Token.FunctionCall(0, DefaultFunctions.ABS)
        val expression = listOf(
            functionCall,
            Token.Bracket.Left,
            Token.Bracket.Right
        )

        assertEquals(
            Expression.FunctionCall(
                functionCall,
                emptyList()
            ),
            subject.parse(expression)
        )
    }

    @Test
    fun parse_function_call_with_one_argument() {
        val functionCall = Token.FunctionCall(1, DefaultFunctions.MIN)
        val expression = listOf(
            functionCall,
            Token.Bracket.Left,
            Token.Operand.Number(1),
            Token.Bracket.Right
        )

        assertEquals(
            Expression.FunctionCall(
                functionCall,
                listOf(Expression.Terminal(Token.Operand.Number(1)))
            ),
            subject.parse(expression)
        )
    }

    @Test
    fun parse_function_call_with_several_argument() {
        val functionCall = Token.FunctionCall(3, DefaultFunctions.MIN)
        val expression = listOf(
            functionCall,
            Token.Bracket.Left,
            Token.Operand.Number(1),
            Token.FunctionCall.Delimiter,
            Token.Operand.Number(2),
            Token.Operator.Plus,
            Token.Operand.Number(3),
            Token.FunctionCall.Delimiter,
            Token.Operand.Number(4),
            Token.Bracket.Right
        )

        assertEquals(
            Expression.FunctionCall(
                functionCall,
                listOf(
                    Expression.Terminal(Token.Operand.Number(1)),
                    Expression.Binary(
                        Token.Operator.Plus,
                        Expression.Terminal(Token.Operand.Number(2)),
                        Expression.Terminal(Token.Operand.Number(3))
                    ),
                    Expression.Terminal(Token.Operand.Number(4)),
                )
            ),
            subject.parse(expression)
        )
    }
}