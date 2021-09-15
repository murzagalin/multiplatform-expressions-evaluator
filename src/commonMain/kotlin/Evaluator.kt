import kotlin.math.pow

class Evaluator {

    fun evaluate(postfixExpression: List<Token>): Double {
        val temp = ArrayDeque<Token>()

        for (token in postfixExpression) {
            val newToken = when (token) {
                is Token.Operand -> token
                is Token.Operator.Sum -> Token.Operand(temp.popLastOperand.value + temp.popLastOperand.value)
                is Token.Operator.Sub -> Token.Operand(- temp.popLastOperand.value + temp.popLastOperand.value)
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
                is Token.Operator.UnaryMinus -> Token.Operand(- temp.popLastOperand.value)
                is Token.Operator.UnaryPlus -> temp.popLastOperand
                is Token.Bracket -> error("Brackets must not appear in postfix expressions")
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