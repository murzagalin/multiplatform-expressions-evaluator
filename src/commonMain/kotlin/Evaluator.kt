import kotlin.math.*

class Evaluator {

    fun evaluate(postfixExpression: List<Token>, values: Map<String, Double> = emptyMap()): Double {
        val temp = ArrayDeque<Token>()

        for (token in postfixExpression) {
            val newToken = when (token) {
                is Token.Operand -> token
                is Token.Variable -> {
                    val value = requireNotNull(values[token.value]) {
                        "Could not resolve variable '${token.value}'"
                    }
                    Token.Operand(value)
                }
                is Token.Operator.Sum -> Token.Operand(temp.popLastOperand.value + temp.popLastOperand.value)
                is Token.Operator.Sub -> Token.Operand(-temp.popLastOperand.value + temp.popLastOperand.value)
                is Token.Operator.Mult -> Token.Operand(temp.popLastOperand.value * temp.popLastOperand.value)
                is Token.Operator.Div -> {
                    val denominator = temp.popLastOperand.value
                    val numerator = temp.popLastOperand.value
                    Token.Operand(numerator / denominator)
                }
                is Token.Operator.Pow -> {
                    val power = temp.popLastOperand.value
                    val base = temp.popLastOperand.value
                    Token.Operand(base.pow(power))
                }
                is Token.Operator.UnaryMinus -> Token.Operand(-temp.popLastOperand.value)
                is Token.Operator.UnaryPlus -> temp.popLastOperand
                is Token.Function.Cos -> Token.Operand(cos(temp.popLastOperand.value))
                is Token.Function.Ln -> Token.Operand(ln(temp.popLastOperand.value))
                is Token.Function.Tan -> Token.Operand(tan(temp.popLastOperand.value))
                is Token.Function.Sin -> Token.Operand(sin(temp.popLastOperand.value))
                is Token.Function.Log -> {
                    val base = temp.popLastOperand.value
                    val argument = temp.popLastOperand.value
                    Token.Operand(log(argument, base))
                }
                is Token.Bracket -> error("Brackets must not appear in postfix expressions")
                is Token.Function.Delimeter -> error("Function delimiters must not appear in postfix expressions")
            }

            temp.add(newToken)
        }

        require(temp.size == 1) { "malformed expression" }

        return temp.popLastOperand.value
    }

    private val ArrayDeque<Token>.popLastOperand: Token.Operand
        get() {
            val last = removeLast()
            require(last is Token.Operand)

            return last
        }
}