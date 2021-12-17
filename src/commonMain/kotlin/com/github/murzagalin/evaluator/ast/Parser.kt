package com.github.murzagalin.evaluator.ast

import com.github.murzagalin.evaluator.Token


/* the parser uses these rules

* expression -> ternary_if
* ternary_if -> logic_or ( "?" ternary_if ":" ternary_if )?
* logic_or   -> logic_and ( ( "or" | "||" ) logic_and )*
* logic_and  -> equality ( ( "and" | "&&" ) equality )*
* equality   -> comparison ( ( "!=" | "==" ) comparison )*
* comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )*
* term       -> factor ( ( "-" | "+" ) factor )*
* factor     -> unary ( ( "/" | "*" | "%") unary )*
* unary      -> ( "!" | "-" | "+" ) unary | exponent
* exponent   -> identifier ( "^" unary )*
- identifier -> function | variable | number | boolean

function   -> name "(" ( arguments )* ")"
arguments  -> expression ( ',' expression )*
* variable   -> name
* name       -> CHAR ( CHAR | DIGIT )*
* boolean    -> "true" | "false" | "TRUE" | "FALSE"
* number     -> DIGIT ( DIGIT )*
*/

class Parser {

    private val equalityTokens = setOf(Token.Operator.Equal, Token.Operator.NotEqual)

    private val comparisonTokens = setOf(
        Token.Operator.GreaterThan,
        Token.Operator.GreaterEqualThan,
        Token.Operator.LessThan,
        Token.Operator.LessEqualThan
    )

    private val termTokens = setOf(Token.Operator.Plus, Token.Operator.Minus)

    private val factorTokens = setOf(Token.Operator.Division, Token.Operator.Multiplication, Token.Operator.Modulo)

    private val unaryTokens = setOf(Token.Operator.UnaryPlus, Token.Operator.UnaryMinus, Token.Operator.Not)

    private var ix = 0

    fun parse(tokens: List<Token>): Expression {
        ix = 0
        return expression(tokens)
    }

    fun expression(tokens: List<Token>): Expression {
        return ternaryIf(tokens)
    }

    private fun ternaryIf(tokens: List<Token>): Expression {
        val first = logicOr(tokens)

        if (ix < tokens.size && tokens[ix] is Token.Operator.TernaryIf) {
            ix++
            val second = ternaryIf(tokens)
            require(tokens[ix++] is Token.Operator.TernaryElse) { "':' expected in ternary-if-else expression" }
            val third = ternaryIf(tokens)

            return Expression.Ternary(Token.Operator.TernaryIfElse, first, second, third)
        }

        return first
    }

    private fun logicOr(tokens: List<Token>): Expression {
        var left = logicAnd(tokens)

        while (ix < tokens.size && tokens[ix] is Token.Operator.Or) {
            ix++
            val right = logicAnd(tokens)
            left = Expression.Binary(Token.Operator.Or, left, right)
        }

        return left
    }

    private fun logicAnd(tokens: List<Token>): Expression {
        var left = equality(tokens)

        while (ix < tokens.size && tokens[ix] is Token.Operator.And) {
            ix++
            val right = equality(tokens)
            left = Expression.Binary(Token.Operator.And, left, right)
        }

        return left
    }

    private fun equality(tokens: List<Token>): Expression {
        var left = comparison(tokens)

        while (ix < tokens.size && tokens[ix] in equalityTokens) {
            val operator = tokens[ix++]
            val right = comparison(tokens)
            left = Expression.Binary(operator as Token.Operator, left, right)
        }

        return left
    }

    private fun comparison(tokens: List<Token>): Expression {
        var left = term(tokens)

        while (ix < tokens.size && tokens[ix] in comparisonTokens) {
            val operator = tokens[ix++]
            val right = term(tokens)
            left = Expression.Binary(operator as Token.Operator, left, right)
        }

        return left
    }

    private fun term(tokens: List<Token>): Expression {
        var left = factor(tokens)

        while (ix < tokens.size && tokens[ix] in termTokens) {
            val operator = tokens[ix++]
            val right = factor(tokens)
            left = Expression.Binary(operator as Token.Operator, left, right)
        }

        return left
    }

    private fun factor(tokens: List<Token>): Expression {
        var left = unary(tokens)

        while (ix < tokens.size && tokens[ix] in factorTokens) {
            val operator = tokens[ix++]
            val right = unary(tokens)
            left = Expression.Binary(operator as Token.Operator, left, right)
        }

        return left
    }

    private fun unary(tokens: List<Token>): Expression {
        return if (ix < tokens.size && tokens[ix] in unaryTokens) {
            val unaryToken = tokens[ix++]

            Expression.Unary(unaryToken as Token.Operator, unary(tokens))
        } else {
            exponent(tokens)
        }
    }

    private fun exponent(tokens: List<Token>): Expression {
        var identifier = identifier(tokens)

        if (ix < tokens.size && tokens[ix] is Token.Operator.Power) {
            ix++
            identifier = Expression.Binary(Token.Operator.Power, identifier, unary(tokens))
        }

        return identifier
    }

    private fun identifier(tokens: List<Token>): Expression {
        require(tokens.size > ix) { "Expression expected" }

        return when (val token = tokens[ix++]) {
            is Token.Operand -> Expression.Terminal(token)
            is Token.Bracket.Left -> {
                val result = expression(tokens)
                require(tokens[ix++] is Token.Bracket.Right) { "')' expected after expression" }
                result
            }
            else -> throw IllegalArgumentException("Expression expected")
        }
    }
}