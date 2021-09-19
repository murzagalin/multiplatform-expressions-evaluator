class Tokenizer(
    private val doubleDelimiter: Char = '.',
    private val argumentsDelimiter: Char = ','
) {

    companion object {
        val allFunctions = mapOf(
            "cos" to Token.Function.Cos,
            "sin" to Token.Function.Sin,
            "tan" to Token.Function.Tan,
            "ln" to Token.Function.Ln,
            "log" to Token.Function.Log
        )

        private val digitChars = ('0'..'9').toSet()
        private val letterChars = ('A'..'Z').toSet() + ('a'..'z').toSet() + '_'
        private val functionKeys = allFunctions.keys
    }

    fun tokenize(expression: String): List<Token> {
        val result = mutableListOf<Token>()

        var ix = 0

        while (ix < expression.length) {
            val symbol = expression[ix]
            val restOfExpression = expression.substring(ix)

            val nextToken = when {
                symbol in digitChars -> {
                    var lastIxOfNumber = restOfExpression.indexOfFirst { it !in (digitChars + doubleDelimiter) }
                    if (lastIxOfNumber == -1) lastIxOfNumber = restOfExpression.length
                    val strNum = restOfExpression.substring(0, lastIxOfNumber)
                    val parsedNumber = requireNotNull(strNum.toDoubleOrNull()) { "error parsing number '$strNum'" }
                    ix += lastIxOfNumber - 1
                    Token.Operand.Num(parsedNumber)
                }
                symbol in letterChars -> {
                    val functionUsed = functionKeys.find { restOfExpression.startsWith("$it(") }
                    if (functionUsed != null) {
                        ix += functionUsed.length - 1
                        allFunctions[functionUsed]
                    } else {
                        var lastIxOfVar = restOfExpression.indexOfFirst {
                            it !in letterChars && it !in digitChars
                        }
                        if (lastIxOfVar == -1) lastIxOfVar = restOfExpression.length

                        ix += lastIxOfVar - 1
                        Token.Operand.Variable(restOfExpression.substring(0, lastIxOfVar))
                    }
                }
                symbol == argumentsDelimiter -> Token.Function.Delimiter
                symbol == '+' -> if (supposedToBeUnaryOperator(result)) Token.Operator.UnaryPlus else Token.Operator.Sum
                symbol == '-' -> if (supposedToBeUnaryOperator(result)) Token.Operator.UnaryMinus else Token.Operator.Sub
                symbol == '*' -> Token.Operator.Mult
                symbol == '/' -> Token.Operator.Div
                symbol == '^' -> Token.Operator.Pow
                symbol == '(' -> Token.Bracket.Left
                symbol == ')' -> Token.Bracket.Right
                restOfExpression.startsWith("&&") -> {
                    ix++
                    Token.Operator.And
                }
                restOfExpression.startsWith("||") -> {
                    ix++
                    Token.Operator.Or
                }
                restOfExpression.startsWith("<=") -> {
                    ix++
                    Token.Operator.LessEqualThan
                }
                restOfExpression.startsWith(">=") -> {
                    ix++
                    Token.Operator.GreaterEqualThan
                }
                restOfExpression.startsWith("==") -> {
                    ix++
                    Token.Operator.Equal
                }
                restOfExpression.startsWith("!=") -> {
                    ix++
                    Token.Operator.NotEqual
                }
                symbol == '<' -> Token.Operator.LessThan
                symbol == '>' -> Token.Operator.GreaterThan
                symbol == '!' -> Token.Operator.Not
                else -> null
            }

            if (nextToken != null) result.add(nextToken)
            ix++
        }

        return result
    }

    private fun supposedToBeUnaryOperator(result: MutableList<Token>): Boolean {
        return result.isEmpty() ||
                result.last() !is Token.Operand.Num &&
                result.last() !is Token.Bracket.Right &&
                result.last() !is Token.Operand.Variable
    }
}