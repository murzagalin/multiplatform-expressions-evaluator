package com.murzagalin.evaluator

internal class Tokenizer(
    private val doubleDelimiter: Char = '.',
    private val argumentsDelimiter: Char = ',',
    functions: List<Function> = DefaultFunctions.ALL
) {

    companion object {
        private val digitChars = ('0'..'9').toSet()
        private val letterChars = ('A'..'Z').toSet() + ('a'..'z').toSet() + '_'
    }

    private val functionsMap = functions.associateBy { it.name }

    fun tokenize(expression: String): List<Token> {
        val parsed = parse(expression)

        require(parsed.indexOffset == expression.length) {
            "error parsing the expression"
        }

        return parsed.tokens
    }

    private fun parse(expression: String): ParsedUnit {
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

        return ParsedUnit(result, indexOffset)
    }

    private fun parseNextUnit(restOfExpression: String, result: List<Token>): ParsedUnit? {
        val symbol = restOfExpression.first()

        return when {
            restOfExpression.startsWith("true") -> ParsedUnit(Token.Operand.Bool(true), 4)
            restOfExpression.startsWith("false") -> ParsedUnit(Token.Operand.Bool(false), 5)
            restOfExpression.startsWith("&&") -> ParsedUnit(Token.Operator.And, 2)
            restOfExpression.startsWith("||") -> ParsedUnit(Token.Operator.Or, 2)
            restOfExpression.startsWith("<=") -> ParsedUnit(Token.Operator.LessEqualThan, 2)
            restOfExpression.startsWith(">=") -> ParsedUnit(Token.Operator.GreaterEqualThan, 2)
            restOfExpression.startsWith("==") -> ParsedUnit(Token.Operator.Equal, 2)
            restOfExpression.startsWith("!=") -> ParsedUnit(Token.Operator.NotEqual, 2)
            symbol in digitChars -> restOfExpression.parseStartingNumber()
            symbol in letterChars -> restOfExpression.parseVarOrFunction()
            symbol == argumentsDelimiter -> ParsedUnit(Token.FunctionCall.Delimiter, 1)
            symbol == '+' -> getPlus(result)
            symbol == '-' -> getMinus(result)
            symbol == '%' -> ParsedUnit(Token.Operator.Mod, 1)
            symbol == '*' -> ParsedUnit(Token.Operator.Mult, 1)
            symbol == '/' -> ParsedUnit(Token.Operator.Div, 1)
            symbol == '^' -> ParsedUnit(Token.Operator.Pow, 1)
            symbol == '(' -> ParsedUnit(Token.Bracket.Left, 1)
            symbol == ')' -> ParsedUnit(Token.Bracket.Right, 1)
            symbol == '<' -> ParsedUnit(Token.Operator.LessThan, 1)
            symbol == '>' -> ParsedUnit(Token.Operator.GreaterThan, 1)
            symbol == '!' -> ParsedUnit(Token.Operator.Not, 1)
            symbol == '?' -> ParsedUnit(Token.Operator.TernaryIf, 1)
            symbol == ':' -> ParsedUnit(Token.Operator.TernaryElse, 1)
            else -> null
        }
    }

    private fun getPlus(result: List<Token>) = ParsedUnit(
        if (supposedToBeUnaryOperator(result)) Token.Operator.UnaryPlus else Token.Operator.Sum,
        1
    )

    private fun getMinus(result: List<Token>) = ParsedUnit(
        if (supposedToBeUnaryOperator(result)) Token.Operator.UnaryMinus else Token.Operator.Sub,
        1
    )

    private fun String.parseStartingNumber(): ParsedUnit {
        var lastIxOfNumber = indexOfFirst { it !in (digitChars + doubleDelimiter) }
        if (lastIxOfNumber == -1) lastIxOfNumber = length
        val strNum = substring(0, lastIxOfNumber)
        val parsedNumber = requireNotNull(strNum.toDoubleOrNull()) { "error parsing number '$strNum'" }

        return ParsedUnit(Token.Operand.Num(parsedNumber), lastIxOfNumber)
    }

    private fun String.parseVarOrFunction(): ParsedUnit {
        var lastIxOfName = indexOfFirst { it !in letterChars && it !in digitChars }

        return if (lastIxOfName != -1 && get(lastIxOfName) == '(') {
            val functionName = substring(0, lastIxOfName)
            val function = requireNotNull(functionsMap[functionName]) { "function not found $functionName" }
            val argsCount = getArgsCount(this, functionName)
            require(argsCount in function.argsCount) { "function $functionName is called with wrong number of parameters" }

            ParsedUnit(Token.FunctionCall(argsCount, function), functionName.length)
        } else {
            if (lastIxOfName == -1) lastIxOfName = length

            ParsedUnit(Token.Operand.Variable(substring(0, lastIxOfName)), lastIxOfName)
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
                result.last() !is Token.Operand.Num &&
                result.last() !is Token.Bracket.Right &&
                result.last() !is Token.Operand.Variable
    }

    private data class ParsedUnit(
        val tokens: List<Token>,
        val indexOffset: Int
    ) {
        constructor(token: Token, indexOffset: Int): this(listOf(token), indexOffset)
    }
}


