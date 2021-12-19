package com.github.murzagalin.evaluator.ast

import com.github.murzagalin.evaluator.Token

sealed class Expression {
    data class Unary(
        val token: Token.Operator,
        val expression: Expression
    ) : Expression()

    data class Binary(
        val token: Token.Operator,
        val leftExpression: Expression,
        val rightExpression: Expression
    ) : Expression()

    data class Ternary(
        val token: Token.Operator,
        val firstExpression: Expression,
        val secondExpression: Expression,
        val thirdExpression: Expression
    ) : Expression()

    data class FunctionCall(
        val token: Token.FunctionCall,
        val arguments: List<Expression>
    ) : Expression()

    data class Terminal(
        val token: Token.Operand
    ) : Expression()
}
