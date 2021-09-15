

class Converter {

    private val operators = ArrayDeque<Token.Operator>()

    fun convert(expression: List<Token>): List<Token> {
        val postfix = mutableListOf<Token>()

        for (token in expression) {
            when (token) {
                is Token.Operand -> postfix.add(token)
                is Token.Operator -> {
                    while (
                        operators.isNotEmpty() &&
                        (token.priority < operators.last().priority ||
                                token.priority == operators.last().priority && token.isLeftAssociative)
                    ) postfix.add(operators.removeLast())
                    operators.addLast(token)
                }
            }
        }

        while (operators.isNotEmpty()) {
            postfix.add(operators.removeLast())
        }

        return postfix
    }

    private val Token.Operator.isLeftAssociative: Boolean
        get() = this.associativity == Token.Associativity.LEFT
}