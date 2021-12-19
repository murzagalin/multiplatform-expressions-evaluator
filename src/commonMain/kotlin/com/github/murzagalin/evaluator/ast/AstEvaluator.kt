package com.github.murzagalin.evaluator.ast

import com.github.murzagalin.evaluator.Token
import kotlin.math.pow

class AstEvaluator(private val values: Map<String, Any> = emptyMap()): AstVisitor {

    fun evaluate(expression: Expression) = expression.visit(this)

    override fun visitTerminal(terminal: Expression.Terminal) = when(val operand = terminal.token) {
        is Token.Operand.Number -> operand.value
        is Token.Operand.Boolean -> operand.value
        is Token.Operand.Variable -> requireNotNull(values[operand.value]) {
            "Could not resolve variable '${operand.value}'"
        }
    }

    override fun visitUnary(unary: Expression.Unary): Any {
        val literal = evaluate(unary.expression)

        return when (unary.token) {
            Token.Operator.UnaryPlus -> {
                require(literal is Number) { "A Number is expected after a unary plus" }
                literal
            }
            Token.Operator.UnaryMinus -> {
                require(literal is Number) { "A Number is expected after a unary plus" }
                -literal.toDouble()
            }
            Token.Operator.Not -> {
                require(literal is Boolean) { "A Number is expected after a unary plus" }
                !literal
            }
            else -> {
                error("${unary.token} was incorrectly parsed as a unary operator")
            }
        }
    }

    override fun visitBinary(binary: Expression.Binary): Any {
        return when (binary.token) {
            Token.Operator.LessThan -> binaryOnNumbers(binary,"<") { left, right -> left < right }
            Token.Operator.LessEqualThan -> binaryOnNumbers(binary,"<=") { left, right -> left <= right }
            Token.Operator.GreaterThan -> binaryOnNumbers(binary,">") { left, right -> left > right }
            Token.Operator.GreaterEqualThan -> binaryOnNumbers(binary,">=") { left, right -> left >= right }
            Token.Operator.Equal -> {
                val left = evaluate(binary.leftExpression)
                val right = evaluate(binary.rightExpression)
                left == right
            }
            Token.Operator.NotEqual -> {
                val left = evaluate(binary.leftExpression)
                val right = evaluate(binary.rightExpression)
                left != right
            }
            Token.Operator.And -> binaryOnBooleans(binary, "&&") { left, right -> left && right}
            Token.Operator.Or -> binaryOnBooleans(binary, "||") { left, right -> left || right}
            Token.Operator.Plus -> binaryOnNumbers(binary, "+") { left, right -> left + right }
            Token.Operator.Minus -> binaryOnNumbers(binary,"-") { left, right -> left - right }
            Token.Operator.Modulo -> binaryOnNumbers(binary,"%") { left, right -> left % right }
            Token.Operator.Multiplication -> binaryOnNumbers(binary,"*") { left, right -> left * right }
            Token.Operator.Division -> binaryOnNumbers(binary,"/") { left, right -> left / right }
            Token.Operator.Power -> binaryOnNumbers(binary,"^") { left, right -> left.pow(right) }
            else -> {
                error("${binary.token} was incorrectly parsed as a binary operator")
            }
        }
    }

    private fun binaryOnBooleans(binary: Expression.Binary, strRep: String, eval: (Boolean, Boolean) -> Any): Any {
        val left = evaluate(binary.leftExpression)
        val right = evaluate(binary.rightExpression)
        require(left is Boolean && right is Boolean) { "'$strRep' must be called with boolean operands" }

        return eval(left, right)
    }

    private fun binaryOnNumbers(binary: Expression.Binary, strRep: String, eval: (Double, Double) -> Any): Any {
        val left = evaluate(binary.leftExpression)
        val right = evaluate(binary.rightExpression)
        require(left is Number && right is Number) { "'$strRep' must be called with number operands" }

        return eval(left.toDouble(), right.toDouble())
    }

    override fun visitTernary(ternary: Expression.Ternary): Any {
        if (ternary.token is Token.Operator.TernaryIfElse) {
            val left = evaluate(ternary.firstExpression)
            require(left is Boolean) {
                "Ternary <condition> ? <expression1> : <expression2> must be called with a boolean value as a condition"
            }
            return if (left) {
                evaluate(ternary.secondExpression)
            } else {
                evaluate(ternary.thirdExpression)
            }
        } else {
            error("${ternary.token} was incorrectly parsed as a ternary operator")
        }
    }

    override fun visitFunctionCall(functionCall: Expression.FunctionCall): Any {
        val arguments = mutableListOf<Any>()

        for (arg in functionCall.arguments) {
            arguments.add(evaluate(arg))
        }

        return functionCall.token(arguments)
    }

}