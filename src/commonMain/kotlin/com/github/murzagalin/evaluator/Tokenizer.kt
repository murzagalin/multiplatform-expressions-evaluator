package com.github.murzagalin.evaluator

internal class Tokenizer(
    private val doubleDelimiter: Char = '.',
    private val argumentsDelimiter: Char = ',',
    functions: List<Function> = DefaultFunctions.ALL,
    constants: List<Constant> = DefaultConstants.ALL
) {

    companion object {
        private val digitChars = ('0'..'9').toSet()
        private val letterChars = ('A'..'Z').toSet() + ('a'..'z').toSet() + '_' + '$' + '@'
    }

    private val functionsMap = functions.associateBy { it.name }
    private val constantsMap = constants.associate { it.name to it.value }

    fun tokenize(expression: String): List<Token> {
        val parsed = parse(expression)

        require(parsed.indexOffset == expression.length) {
            "error parsing the expression"
        }

        return parsed.tokens
    }

    private fun parse(expression: String): PUnit {
        val result = mutableListOf<Token>()
        var indexOffset = 0

        while (indexOffset < expression.length) {
            val restOfExpression = expression.substring(indexOffset)

            val unit = parseNextUnit(restOfExpression, result)

            if (unit != null) {
                result.addAll(unit.tokens)
                indexOffset += unit.indexOffset
            } else {
                indexOffset++
            }
        }

        return PUnit(result, indexOffset)
    }

    private fun parseNextUnit(str: String, result: List<Token>): PUnit? {
        val symbol = str.first()

        return when {
            str.startsWith("true") -> PUnit(Token.Operand.Boolean(true), 4)
            str.startsWith("false") -> PUnit(Token.Operand.Boolean(false), 5)
            str.startsWith("&&") -> PUnit(Token.Operator.And, 2)
            str.startsWith("||") -> PUnit(Token.Operator.Or, 2)
            str.startsWith("<=") -> PUnit(Token.Operator.LessEqualThan, 2)
            str.startsWith(">=") -> PUnit(Token.Operator.GreaterEqualThan, 2)
            str.startsWith("==") -> PUnit(Token.Operator.Equal, 2)
            str.startsWith("!=") -> PUnit(Token.Operator.NotEqual, 2)
            symbol in digitChars -> str.parseStartingNumber()
            symbol in letterChars -> str.parseVarOrConstOrFunction()
            symbol == argumentsDelimiter -> PUnit(Token.FunctionCall.Delimiter, 1)
            symbol == '+' -> getPlus(result)
            symbol == '-' -> getMinus(result)
            symbol == '%' -> PUnit(Token.Operator.Modulo, 1)
            symbol == '*' -> PUnit(Token.Operator.Multiplication, 1)
            symbol == '/' -> PUnit(Token.Operator.Division, 1)
            symbol == '^' -> PUnit(Token.Operator.Power, 1)
            symbol == ')' -> PUnit(Token.Bracket.Right, 1)
            symbol == '(' -> PUnit(Token.Bracket.Left, 1)
            symbol == '<' -> PUnit(Token.Operator.LessThan, 1)
            symbol == '>' -> PUnit(Token.Operator.GreaterThan, 1)
            symbol == '!' -> PUnit(Token.Operator.Not, 1)
            symbol == '?' -> PUnit(Token.Operator.TernaryIf, 1)
            symbol == ':' -> PUnit(Token.Operator.TernaryElse, 1)
            else -> null
        }
    }

    private fun getPlus(result: List<Token>) = PUnit(
        if (supposedToBeUnaryOperator(result)) Token.Operator.UnaryPlus else Token.Operator.Plus,
        1
    )

    private fun getMinus(result: List<Token>) = PUnit(
        if (supposedToBeUnaryOperator(result)) Token.Operator.UnaryMinus else Token.Operator.Minus,
        1
    )

    private fun String.parseStartingNumber(): PUnit {
        var lastIxOfNumber = indexOfFirst { it !in (digitChars + doubleDelimiter) }
        if (lastIxOfNumber == -1) lastIxOfNumber = length
        val strNum = substring(0, lastIxOfNumber)
        val parsedNumber = requireNotNull(strNum.toDoubleOrNull()) { "error parsing number '$strNum'" }

        return PUnit(Token.Operand.Number(parsedNumber), lastIxOfNumber)
    }

    private fun String.parseVarOrConstOrFunction(): PUnit {
        var lastIxOfName = indexOfFirst { it !in letterChars && it !in digitChars }
        if (lastIxOfName == -1) lastIxOfName = length
        val name = substring(0, lastIxOfName)
        val constant = constantsMap[name]

        return when {
            lastIxOfName != length && get(lastIxOfName) == '(' -> {
                val function = requireNotNull(functionsMap[name]) { "function not found $name" }
                val argsCount = getArgsCount(this, name)
                require(argsCount in function.argsCount) { "function $name is called with wrong number of parameters" }

                PUnit(Token.FunctionCall(argsCount, function), name.length)
            }
            constant != null -> PUnit(Token.Operand.Number(constant), lastIxOfName)
            else -> PUnit(Token.Operand.Variable(substring(0, lastIxOfName)), lastIxOfName)
        }
    }

    private fun getArgsCount(expression: String, functionName: String): Int {
        val expressionAfterFunction = expression.substring(functionName.length)
        var bracketsCounter = 0
        var delimitersCounter = 0

        for (symbol in expressionAfterFunction) {
            when (symbol) {
                '(' -> bracketsCounter++
                ')' -> bracketsCounter--
                argumentsDelimiter -> if (bracketsCounter == 1) delimitersCounter++
            }
            if (bracketsCounter == 0) break
        }

        return delimitersCounter + 1
    }

    private fun supposedToBeUnaryOperator(result: List<Token>): Boolean {
        return result.isEmpty() ||
                result.last() !is Token.Operand.Number &&
                result.last() !is Token.Bracket.Right &&
                result.last() !is Token.Operand.Variable
    }

    private data class PUnit(
        val tokens: List<Token>,
        val indexOffset: Int
    ) {
        constructor(token: Token, indexOffset: Int): this(listOf(token), indexOffset)
    }
}


