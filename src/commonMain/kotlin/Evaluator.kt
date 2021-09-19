import kotlin.math.*

class Evaluator {

    fun evaluate(postfixExpression: List<Token>, values: Map<String, Double> = emptyMap()): Double {
        val temp = ArrayDeque<Token>()

        for (token in postfixExpression) {
            val newToken = when (token) {
                is Token.Operand.Num -> token
                is Token.Operand.Variable -> {
                    val value = requireNotNull(values[token.value]) {
                        "Could not resolve variable '${token.value}'"
                    }
                    Token.Operand.Num(value)
                }
                is Token.Operator.Sum -> Token.Operand.Num(temp.popLastOperand.value + temp.popLastOperand.value)
                is Token.Operator.Sub -> Token.Operand.Num(-temp.popLastOperand.value + temp.popLastOperand.value)
                is Token.Operator.Mult -> Token.Operand.Num(temp.popLastOperand.value * temp.popLastOperand.value)
                is Token.Operator.Div -> {
                    val denominator = temp.popLastOperand.value
                    val numerator = temp.popLastOperand.value
                    Token.Operand.Num(numerator / denominator)
                }
                is Token.Operator.Pow -> {
                    val power = temp.popLastOperand.value
                    val base = temp.popLastOperand.value
                    Token.Operand.Num(base.pow(power))
                }
                is Token.Operator.UnaryMinus -> Token.Operand.Num(-temp.popLastOperand.value)
                is Token.Operator.UnaryPlus -> temp.popLastOperand
                is Token.Function.Cos -> Token.Operand.Num(cos(temp.popLastOperand.value))
                is Token.Function.Ln -> Token.Operand.Num(ln(temp.popLastOperand.value))
                is Token.Function.Tan -> Token.Operand.Num(tan(temp.popLastOperand.value))
                is Token.Function.Sin -> Token.Operand.Num(sin(temp.popLastOperand.value))
                is Token.Function.Log -> {
                    val base = temp.popLastOperand.value
                    val argument = temp.popLastOperand.value
                    Token.Operand.Num(log(argument, base))
                }
                is Token.Bracket -> error("Brackets must not appear in postfix expressions")
                is Token.Function.Delimiter -> error("Function delimiters must not appear in postfix expressions")
            }

            temp.add(newToken)
        }

        require(temp.size == 1) { "malformed expression" }

        return temp.popLastOperand.value
    }

    private val ArrayDeque<Token>.popLastOperand: Token.Operand.Num
        get() {
            val last = removeLast()
            require(last is Token.Operand.Num)

            return last
        }
}