
class Tokenizer {
    private val digitChars = '0'..'9'

    fun tokenize(expression: String): List<Token> {
        val result = mutableListOf<Token>()

        var ix = 0
        var number: Int? = null
        var nextToken: Token? = null

        while (ix < expression.length) {
            val symbol = expression[ix]

            when (symbol) {
                in digitChars -> {
                    number = if (number == null) symbol.digitToInt() else number * 10 + symbol.digitToInt()
                }
                '+' -> nextToken = Token.Operator.Sum
                '*' -> nextToken = Token.Operator.Mult
                '-' -> nextToken = Token.Operator.Sub
                '/' -> nextToken = Token.Operator.Div
                '^' -> nextToken = Token.Operator.Pow
            }

            ix++

            if (number != null && (symbol !in digitChars || ix >= expression.length)) {
                result.add(Token.Operand.NInteger(number))
                number = null
            }

            if (nextToken != null) {
                result.add(nextToken)
                nextToken = null
            }
        }

        return result
    }
}