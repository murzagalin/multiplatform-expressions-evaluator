package com.murzagalin.evaluator

import kotlin.math.*

abstract class BaseEvaluator {

    protected fun evaluateInternal(postfixExpression: List<Token>, values: Map<String, Any> = emptyMap()): Token {
        val temp = ArrayDeque<Token>()

        for (token in postfixExpression) {
            val newToken = when (token) {
                is Token.Operand.Num -> token
                is Token.Operand.Bool -> token
                is Token.Operand.Variable -> {
                    val value = requireNotNull(values[token.value]) { "Could not resolve variable '${token.value}'" }
                    resolveVar(token.value, value)
                }
                is Token.Operator.LessThan -> {
                    val right = temp.popLastNum.value
                    val left = temp.popLastNum.value
                    Token.Operand.Bool(left < right)
                }
                is Token.Operator.LessEqualThan -> {
                    val right = temp.popLastNum.value
                    val left = temp.popLastNum.value
                    Token.Operand.Bool(left <= right)
                }
                is Token.Operator.GreaterThan -> {
                    val right = temp.popLastNum.value
                    val left = temp.popLastNum.value
                    Token.Operand.Bool(left > right)
                }
                is Token.Operator.GreaterEqualThan -> {
                    val right = temp.popLastNum.value
                    val left = temp.popLastNum.value
                    Token.Operand.Bool(left >= right)
                }
                is Token.Operator.Equal -> {
                    val right = temp.removeLast()
                    val left = temp.removeLast()
                    Token.Operand.Bool(left == right)
                }
                is Token.Operator.NotEqual -> {
                    val right = temp.removeLast()
                    val left = temp.removeLast()
                    Token.Operand.Bool(left != right)
                }
                is Token.Operator.And -> {
                    val right = temp.popLastBool.value
                    val left = temp.popLastBool.value
                    Token.Operand.Bool(left && right)
                }
                is Token.Operator.Or -> {
                    val right = temp.popLastBool.value
                    val left = temp.popLastBool.value
                    Token.Operand.Bool(left || right)
                }
                is Token.Operator.Not -> Token.Operand.Bool(!temp.popLastBool.value)
                is Token.Operator.Sum -> Token.Operand.Num(temp.popLastNum.value + temp.popLastNum.value)
                is Token.Operator.Sub -> Token.Operand.Num(-temp.popLastNum.value + temp.popLastNum.value)
                is Token.Operator.Mod ->  {
                    val denominator = temp.popLastNum.value
                    val numerator = temp.popLastNum.value

                    Token.Operand.Num(numerator % denominator)
                }
                is Token.Operator.Mult -> Token.Operand.Num(temp.popLastNum.value * temp.popLastNum.value)
                is Token.Operator.Div -> {
                    val denominator = temp.popLastNum.value
                    val numerator = temp.popLastNum.value
                    Token.Operand.Num(numerator / denominator)
                }
                is Token.Operator.Pow -> {
                    val power = temp.popLastNum.value
                    val base = temp.popLastNum.value
                    Token.Operand.Num(base.pow(power))
                }
                is Token.Operator.UnaryMinus -> Token.Operand.Num(-temp.popLastNum.value)
                is Token.Operator.UnaryPlus -> temp.popLastNum
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

                    if (temp.popLastBool.value) ifOp else elseOp
                }
            }

            temp.add(newToken)
        }

        require(temp.size == 1) { "malformed expression" }

        return temp.last()
    }

    private fun resolveVar(varName: Any, value: Any) = when (value) {
        is Boolean -> Token.Operand.Bool(value)
        is Number -> Token.Operand.Num(value.toDouble())
        else -> error("variable type '${value::class.simpleName}' is not supported (variable '$varName')")
    }

    protected val ArrayDeque<Token>.popLastNum: Token.Operand.Num
        get() {
            val last = removeLast()
            require(last is Token.Operand.Num)

            return last
        }

    protected val ArrayDeque<Token>.popLastBool: Token.Operand.Bool
        get() {
            val last = removeLast()
            require(last is Token.Operand.Bool)

            return last
        }

    protected val ArrayDeque<Token>.popLastOperand: Token.Operand
        get() {
            val last = removeLast()
            require(last is Token.Operand)

            return last
        }
}