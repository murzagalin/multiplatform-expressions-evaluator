package com.github.murzagalin.evaluator.ast

import com.github.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserTernaryIfTests {

    private val subject = Parser()

    @Test
    fun simple_ternary_if() {
        val expression = listOf(
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("b"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("c")
        )

        assertEquals(
            Expression.Ternary(
                Token.Operator.TernaryIfElse,
                Expression.Terminal(Token.Operand.Variable("a")),
                Expression.Terminal(Token.Operand.Variable("b")),
                Expression.Terminal(Token.Operand.Variable("c"))
            ),
            subject.parse(expression)
        )
    }

    @Test
    fun parse_with_ternary_if_with_sum_in_condition() {
        val expression = listOf(
            Token.Operand.Variable("z"),
            Token.Operator.Plus,
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("b"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("c")
        )

        assertEquals(
            Expression.Ternary(
                Token.Operator.TernaryIfElse,
                Expression.Binary(
                    Token.Operator.Plus,
                    Expression.Terminal(Token.Operand.Variable("z")),
                    Expression.Terminal(Token.Operand.Variable("a"))
                ),
                Expression.Terminal(Token.Operand.Variable("b")),
                Expression.Terminal(Token.Operand.Variable("c")),
            ),
            subject.parse(expression)
        )
    }

    @Test
    fun convert_sum_with_ternary_if() {
        val expression = listOf(
            Token.Operand.Variable("z"),
            Token.Operator.Plus,
            Token.Bracket.Left,
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("b"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("c"),
            Token.Bracket.Right
        )

        assertEquals(
            Expression.Binary(
                Token.Operator.Plus,
                Expression.Terminal(Token.Operand.Variable("z")),
                Expression.Ternary(
                    Token.Operator.TernaryIfElse,
                    Expression.Terminal(Token.Operand.Variable("a")),
                    Expression.Terminal(Token.Operand.Variable("b")),
                    Expression.Terminal(Token.Operand.Variable("c"))
                )
            ),
            subject.parse(expression)
        )
    }

    @Test
    fun ternary_if_with_ternary_if_1() { //a ? b : x ? y : z --> a b x y z ?: ?:
        val expression = listOf(
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("b"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("x"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("y"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("z")
        )

        assertEquals(
            Expression.Ternary(
                Token.Operator.TernaryIfElse,
                Expression.Terminal(Token.Operand.Variable("a")),
                Expression.Terminal(Token.Operand.Variable("b")),
                Expression.Ternary(
                    Token.Operator.TernaryIfElse,
                    Expression.Terminal(Token.Operand.Variable("x")),
                    Expression.Terminal(Token.Operand.Variable("y")),
                    Expression.Terminal(Token.Operand.Variable("z")),
                )
            ),
            subject.parse(expression)
        )
    }

    @Test
    fun ternary_if_with_ternary_if_2() { // a ? x ? y : z : b  -->  a x y z ?: b ?:
        val expression = listOf(
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("x"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("y"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("z"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("b")
        )

        assertEquals(
            Expression.Ternary(
                Token.Operator.TernaryIfElse,
                Expression.Terminal(Token.Operand.Variable("a")),
                Expression.Ternary(
                    Token.Operator.TernaryIfElse,
                    Expression.Terminal(Token.Operand.Variable("x")),
                    Expression.Terminal(Token.Operand.Variable("y")),
                    Expression.Terminal(Token.Operand.Variable("z")),
                ),
                Expression.Terminal(Token.Operand.Variable("b"))
            ),
            subject.parse(expression)
        )
    }

    @Test
    fun ternary_if_with_ternary_if_3() { // (x ? y : z) ? a : b  -->  x y z ?: a b ?:
        val expression = listOf(
            Token.Bracket.Left,
            Token.Operand.Variable("x"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("y"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("z"),
            Token.Bracket.Right,
            Token.Operator.TernaryIf,
            Token.Operand.Variable("a"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("b")
        )

        assertEquals(
            Expression.Ternary(
                Token.Operator.TernaryIfElse,
                Expression.Ternary(
                    Token.Operator.TernaryIfElse,
                    Expression.Terminal(Token.Operand.Variable("x")),
                    Expression.Terminal(Token.Operand.Variable("y")),
                    Expression.Terminal(Token.Operand.Variable("z")),
                ),
                Expression.Terminal(Token.Operand.Variable("a")),
                Expression.Terminal(Token.Operand.Variable("b"))
            ),
            subject.parse(expression)
        )
    }
}