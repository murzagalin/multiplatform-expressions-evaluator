package com.github.murzagalin.evaluator.ast

import com.github.murzagalin.evaluator.Token

sealed class Expression {

    abstract fun visit(visitor: AstVisitor): Any

    data class Unary(
        val token: Token.Operator,
        val expression: Expression
    ) : Expression() {
        override fun visit(visitor: AstVisitor) = visitor.visitUnary(this)
    }

    data class Binary(
        val token: Token.Operator,
        val leftExpression: Expression,
        val rightExpression: Expression
    ) : Expression() {
        override fun visit(visitor: AstVisitor) = visitor.visitBinary(this)
    }

    data class Ternary(
        val token: Token.Operator,
        val firstExpression: Expression,
        val secondExpression: Expression,
        val thirdExpression: Expression
    ) : Expression() {
        override fun visit(visitor: AstVisitor) = visitor.visitTernary(this)
    }

    data class FunctionCall(
        val token: Token.FunctionCall,
        val arguments: List<Expression>
    ) : Expression() {
        override fun visit(visitor: AstVisitor) = visitor.visitFunctionCall(this)
    }

    data class Terminal(
        val token: Token.Operand
    ) : Expression() {
        override fun visit(visitor: AstVisitor) = visitor.visitTerminal(this)
    }
}


interface AstVisitor {
    fun visitUnary(unary: Expression.Unary): Any
    fun visitBinary(binary: Expression.Binary): Any
    fun visitTernary(ternary: Expression.Ternary): Any
    fun visitFunctionCall(functionCall: Expression.FunctionCall): Any
    fun visitTerminal(terminal: Expression.Terminal): Any
}
