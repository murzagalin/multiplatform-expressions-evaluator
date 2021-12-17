package com.github.murzagalin.evaluator.ast

import com.github.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserArithmeticTests {

    private val subject = Parser()

    @Test
    fun parse_simple_sum() {
        val expression = listOf(
            Token.Operand.Number(4),
            Token.Operator.Plus,
            Token.Operand.Number(3)
        )
        val result = subject.parse(expression)

        assertEquals(
            Expression.Binary(
                Token.Operator.Plus,
                Expression.Terminal(Token.Operand.Number(4)),
                Expression.Terminal(Token.Operand.Number(3))
            ),
            result
        )
    }

    @Test
    fun parse_simple_multiplication() {
        val expression = listOf(
            Token.Operand.Number(4),
            Token.Operator.Multiplication,
            Token.Operand.Number(3)
        )
        val result = subject.parse(expression)

        assertEquals(
            Expression.Binary(
                Token.Operator.Multiplication,
                Expression.Terminal(Token.Operand.Number(4)),
                Expression.Terminal(Token.Operand.Number(3))
            ),
            result
        )
    }

    @Test
    fun parse_simple_multiplication_sum() {
        val expression = listOf(
            Token.Operand.Number(4),
            Token.Operator.Multiplication,
            Token.Operand.Number(3),
            Token.Operator.Plus,
            Token.Operand.Number(2)
        )
        val result = subject.parse(expression)

        val expected = Expression.Binary(
            Token.Operator.Plus,
            Expression.Binary(
                Token.Operator.Multiplication,
                Expression.Terminal(Token.Operand.Number(4)),
                Expression.Terminal(Token.Operand.Number(3))
            ),
            Expression.Terminal(Token.Operand.Number(2))
        )
        assertEquals(expected, result)
    }

    @Test
    fun parse_simple_sum_multiplication() {
        val expression = listOf(
            Token.Operand.Number(2),
            Token.Operator.Plus,
            Token.Operand.Number(4),
            Token.Operator.Multiplication,
            Token.Operand.Number(3)
        )
        val result = subject.parse(expression)

        val expected = Expression.Binary(
            Token.Operator.Plus,
            Expression.Terminal(Token.Operand.Number(2)),
            Expression.Binary(
                Token.Operator.Multiplication,
                Expression.Terminal(Token.Operand.Number(4)),
                Expression.Terminal(Token.Operand.Number(3))
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun parse_multiplication_and_sum_in_brackets() {
        val expression = listOf(
            Token.Operand.Number(4),
            Token.Operator.Multiplication,
            Token.Bracket.Left,
            Token.Operand.Number(3),
            Token.Operator.Plus,
            Token.Operand.Number(2),
            Token.Bracket.Right
        )
        val result = subject.parse(expression)

        val expected = Expression.Binary(
            Token.Operator.Multiplication,
            Expression.Terminal(Token.Operand.Number(4)),
            Expression.Binary(
                Token.Operator.Plus,
                Expression.Terminal(Token.Operand.Number(3)),
                Expression.Terminal(Token.Operand.Number(2))
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun parse_sum_in_brackets_and_multiplication() {
        val expression = listOf(
            Token.Bracket.Left,
            Token.Operand.Number(3),
            Token.Operator.Plus,
            Token.Operand.Number(2),
            Token.Bracket.Right,
            Token.Operator.Multiplication,
            Token.Operand.Number(4),
        )
        val result = subject.parse(expression)

        val expected = Expression.Binary(
            Token.Operator.Multiplication,
            Expression.Binary(
                Token.Operator.Plus,
                Expression.Terminal(Token.Operand.Number(3)),
                Expression.Terminal(Token.Operand.Number(2))
            ),
            Expression.Terminal(Token.Operand.Number(4)),
        )
        assertEquals(expected, result)
    }

    @Test
    fun simple_unary_tests() {
        val expression1 = listOf(
            Token.Operator.UnaryMinus,
            Token.Operand.Number(4),
        )

        val expression2 = listOf(
            Token.Operator.UnaryPlus,
            Token.Operand.Number(4),
        )

        val expression3 = listOf(
            Token.Operator.Not,
            Token.Operand.Boolean(false),
        )

        val expression4 = listOf(
            Token.Operator.UnaryMinus,
            Token.Operator.UnaryMinus,
            Token.Operand.Number(4),
        )

        assertEquals(
            Expression.Unary(
                Token.Operator.UnaryMinus,
                Expression.Terminal(Token.Operand.Number(4)),
            ),
            subject.parse(expression1)
        )
        assertEquals(
            Expression.Unary(
                Token.Operator.UnaryPlus,
                Expression.Terminal(Token.Operand.Number(4)),
            ),
            subject.parse(expression2)
        )
        assertEquals(
            Expression.Unary(
                Token.Operator.Not,
                Expression.Terminal(Token.Operand.Boolean(false)),
            ),
            subject.parse(expression3)
        )
        assertEquals(
            Expression.Unary(
                Token.Operator.UnaryMinus,
                Expression.Unary(
                    Token.Operator.UnaryMinus,
                    Expression.Terminal(Token.Operand.Number(4)),
                ),
            ),
            subject.parse(expression4)
        )
    }

    @Test
    fun operations_with_unary() {
        val expression1 = listOf(
            Token.Operand.Number(3),
            Token.Operator.Plus,
            Token.Operator.UnaryMinus,
            Token.Operand.Number(4),
        )
        val expression2 = listOf(
            Token.Operand.Number(3),
            Token.Operator.Minus,
            Token.Operator.UnaryPlus,
            Token.Operand.Number(4),
        )

        assertEquals(
            Expression.Binary(
                Token.Operator.Plus,
                Expression.Terminal(Token.Operand.Number(3)),
                Expression.Unary(
                    Token.Operator.UnaryMinus,
                    Expression.Terminal(Token.Operand.Number(4)),
                ),
            ),
            subject.parse(expression1)
        )
        assertEquals(
            Expression.Binary(
                Token.Operator.Minus,
                Expression.Terminal(Token.Operand.Number(3)),
                Expression.Unary(
                    Token.Operator.UnaryPlus,
                    Expression.Terminal(Token.Operand.Number(4)),
                ),
            ),
            subject.parse(expression2)
        )
    }


    @Test
    fun series_of_exponent_test() {
        val expression = listOf(
            Token.Operand.Number(2),
            Token.Operator.Power,
            Token.Operand.Number(3),
            Token.Operator.Power,
            Token.Operand.Number(4),
            Token.Operator.Power,
            Token.Operand.Number(5)
        )

        val result = subject.parse(expression)

        assertEquals(
            Expression.Binary(
                Token.Operator.Power,
                Expression.Terminal(Token.Operand.Number(2)),
                Expression.Binary(
                    Token.Operator.Power,
                    Expression.Terminal(Token.Operand.Number(3)),
                    Expression.Binary(
                        Token.Operator.Power,
                        Expression.Terminal(Token.Operand.Number(4)),
                        Expression.Terminal(Token.Operand.Number(5))
                    )
                )
            ),
            result
        )
    }

    @Test
    fun series_of_sum_test() {
        val expression = listOf(
            Token.Operand.Number(2),
            Token.Operator.Plus,
            Token.Operand.Number(3),
            Token.Operator.Plus,
            Token.Operand.Number(4),
            Token.Operator.Plus,
            Token.Operand.Number(5)
        )

        val result = subject.parse(expression)

        assertEquals(
            Expression.Binary(
                Token.Operator.Plus,
                Expression.Binary(
                    Token.Operator.Plus,
                    Expression.Binary(
                        Token.Operator.Plus,
                        Expression.Terminal(Token.Operand.Number(2)),
                        Expression.Terminal(Token.Operand.Number(3))
                    ),
                    Expression.Terminal(Token.Operand.Number(4)),
                ),
                Expression.Terminal(Token.Operand.Number(5))
            ),
            result
        )
    }
}