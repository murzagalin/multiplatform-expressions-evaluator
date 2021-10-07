package com.murzagalin.evaluator

import kotlin.math.*

internal abstract class BaseEvaluator {

    protected fun evaluateInternal(
        postfixExpression: PreprocessedExpression,
        values: Map<String, Any> = emptyMap()
    ): Token {
        val temp = ArrayDeque<Token>()

        for (token in postfixExpression.expression) {
            val newToken = when (token) {
                is Token.Operand.Number -> token
                is Token.Operand.Boolean -> token
                is Token.Operand.Variable -> {
                    val value = requireNotNull(values[token.value]) { "Could not resolve variable '${token.value}'" }
                    resolveVar(token.value, value)
                }
                is Token.Operator.LessThan -> {
                    val right = temp.popLastNumber.value
                    val left = temp.popLastNumber.value
                    Token.Operand.Boolean(left < right)
                }
                is Token.Operator.LessEqualThan -> {
                    val right = temp.popLastNumber.value
                    val left = temp.popLastNumber.value
                    Token.Operand.Boolean(left <= right)
                }
                is Token.Operator.GreaterThan -> {
                    val right = temp.popLastNumber.value
                    val left = temp.popLastNumber.value
                    Token.Operand.Boolean(left > right)
                }
                is Token.Operator.GreaterEqualThan -> {
                    val right = temp.popLastNumber.value
                    val left = temp.popLastNumber.value
                    Token.Operand.Boolean(left >= right)
                }
                is Token.Operator.Equal -> {
                    val right = temp.removeLast()
                    val left = temp.removeLast()
                    Token.Operand.Boolean(left == right)
                }
                is Token.Operator.NotEqual -> {
                    val right = temp.removeLast()
                    val left = temp.removeLast()
                    Token.Operand.Boolean(left != right)
                }
                is Token.Operator.And -> {
                    val right = temp.popLastBoolean.value
                    val left = temp.popLastBoolean.value
                    Token.Operand.Boolean(left && right)
                }
                is Token.Operator.Or -> {
                    val right = temp.popLastBoolean.value
                    val left = temp.popLastBoolean.value
                    Token.Operand.Boolean(left || right)
                }
                is Token.Operator.Not -> Token.Operand.Boolean(!temp.popLastBoolean.value)
                is Token.Operator.Plus -> Token.Operand.Number(temp.popLastNumber.value + temp.popLastNumber.value)
                is Token.Operator.Minus -> Token.Operand.Number(-temp.popLastNumber.value + temp.popLastNumber.value)
                is Token.Operator.Modulo ->  {
                    val denominator = temp.popLastNumber.value
                    val numerator = temp.popLastNumber.value

                    Token.Operand.Number(numerator % denominator)
                }
                is Token.Operator.Multiplication -> Token.Operand.Number(temp.popLastNumber.value * temp.popLastNumber.value)
                is Token.Operator.Division -> {
                    val denominator = temp.popLastNumber.value
                    val numerator = temp.popLastNumber.value
                    Token.Operand.Number(numerator / denominator)
                }
                is Token.Operator.Power -> {
                    val power = temp.popLastNumber.value
                    val base = temp.popLastNumber.value
                    Token.Operand.Number(base.pow(power))
                }
                is Token.Operator.UnaryMinus -> Token.Operand.Number(-temp.popLastNumber.value)
                is Token.Operator.UnaryPlus -> temp.popLastNumber
                is Token.FunctionCall -> {
                    val operands = mutableListOf<Token.Operand>()

                    for (i in 0 until token.argsCount) {
                        operands.add(0, temp.popLastOperand)
                    }

                    token(operands)
                }
                is Token.Bracket -> error("Brackets must not appear in postfix expressions")
                is Token.FunctionCall.Delimiter -> error("Function delimiters must not appear in postfix expressions")
                is Token.Operator.TernaryIf -> error("Ternary if must not appear in postfix expression")
                is Token.Operator.TernaryElse -> error("Ternary else must not appear in postfix expression")
                is Token.Operator.TernaryIfElse -> {
                    val elseOp = temp.popLastOperand
                    val ifOp = temp.popLastOperand
                    require(ifOp::class == elseOp::class) {
                        "malformed ternary if: 'if' operand and 'else' operand have different types"
                    }

                    if (temp.popLastBoolean.value) ifOp else elseOp
                }
            }

            temp.add(newToken)
        }

        require(temp.size == 1) { "malformed expression" }

        return temp.last()
    }

    private fun resolveVar(varName: Any, value: Any) = when (value) {
        is Boolean -> Token.Operand.Boolean(value)
        is Number -> Token.Operand.Number(value.toDouble())
        else -> error("variable type '${value::class.simpleName}' is not supported (variable '$varName')")
    }

    protected val ArrayDeque<Token>.popLastNumber: Token.Operand.Number
        get() {
            val last = removeLast()
            require(last is Token.Operand.Number) {
                "Expected number operand, but token is ${last::class.simpleName}"
            }

            return last
        }

    protected val ArrayDeque<Token>.popLastBoolean: Token.Operand.Boolean
        get() {
            val last = removeLast()
            require(last is Token.Operand.Boolean) {
                "Expected boolean operand, but token is ${last::class.simpleName}"
            }

            return last
        }

    protected val ArrayDeque<Token>.popLastOperand: Token.Operand
        get() {
            val last = removeLast()
            require(last is Token.Operand) {
                "Expected operand, but token is ${last::class.simpleName}"
            }

            return last
        }
}