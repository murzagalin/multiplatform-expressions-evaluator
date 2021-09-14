
class Tokenizer {
    private val digitChars = '0'..'9'

    fun tokenize(expression: String): List<Token> {
        val result = mutableListOf<Token>()

        for (symbol in expression) {
            when {
                symbol in digitChars -> result.add(Token.Operand.NInteger(symbol.digitToInt()))
                symbol == '+' -> result.add(Token.Operator.Sum)
                symbol == '*' -> result.add(Token.Operator.Multiplication)
            }
        }

        return result
    }
}