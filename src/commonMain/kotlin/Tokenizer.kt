class Tokenizer {
    private val digitChars = '0'..'9'
    private val letterChars = ('A'..'Z').toSet() + ('a'..'z').toSet()

    private val allFunctions = Token.Function.allFunctions
    private val functionKeys = allFunctions.keys

    fun tokenize(expression: String): List<Token> {
        val result = mutableListOf<Token>()

        var ix = 0
        var nextToken: Token? = null

        val num = StringBuilder()

        while (ix < expression.length) {
            val symbol = expression[ix]

            when (symbol) {
                in digitChars -> num.append(symbol.digitToInt())
                in letterChars -> {
                    val restOfExpression = expression.substring(ix)
                    val functionUsed = requireNotNull(
                        functionKeys.find { restOfExpression.startsWith("$it(") }
                    ) {
                        "unknown symbol $symbol"
                    }
                    nextToken = allFunctions[functionUsed]
                    ix += functionUsed.length - 1
                }
                ',' -> nextToken = Token.Function.Delimeter
                '.' -> num.append('.')
                '+' -> {
                    nextToken = if (supposedToBeUnaryOperator(result, num.toString())) {
                        Token.Operator.UnaryPlus
                    } else {
                        Token.Operator.Sum
                    }
                }
                '-' -> {
                    nextToken = if (supposedToBeUnaryOperator(result, num.toString())) {
                        Token.Operator.UnaryMinus
                    } else {
                        Token.Operator.Sub
                    }
                }
                '*' -> nextToken = Token.Operator.Mult
                '/' -> nextToken = Token.Operator.Div
                '^' -> nextToken = Token.Operator.Pow
                '(' -> nextToken = Token.Bracket.Left
                ')' -> nextToken = Token.Bracket.Right
            }

            ix++

            if (num.isNotEmpty() && (symbol !in digitChars && symbol != '.' || ix >= expression.length)) {
                val parsedNumber = requireNotNull(num.toString().toDoubleOrNull()) {
                    "error parsing number $num"
                }
                result.add(Token.Operand(parsedNumber))
                num.clear()
            }

            if (nextToken != null) {
                result.add(nextToken)
                nextToken = null
            }
        }

        return result
    }

    private fun supposedToBeUnaryOperator(result: MutableList<Token>, pendingNumber: String): Boolean {
        return (
                result.isEmpty() ||
                        result.last() !is Token.Operand &&
                        result.last() !is Token.Bracket.Right
                ) && pendingNumber.isEmpty()
    }
}