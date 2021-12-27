package com.github.murzagalin.evaluator

import kotlin.jvm.JvmInline

sealed interface Token {

    sealed interface Operand : Token {
        @JvmInline
        value class Number(val value: Double) : Operand {
            constructor(value: Int): this(value.toDouble())
        }

        @JvmInline
        value class Boolean(val value: kotlin.Boolean) : Operand

        @JvmInline
        value class Variable(val value: String) : Operand
    }

    sealed interface Operator : Token {
        object Plus : Operator
        object Minus : Operator
        object Multiplication : Operator
        object Division : Operator
        object Modulo : Operator
        object Power : Operator
        object UnaryMinus : Operator
        object UnaryPlus : Operator

        object And : Operator
        object Or : Operator
        object Not : Operator

        object GreaterThan : Operator //>
        object GreaterEqualThan : Operator //>=
        object LessThan : Operator //<
        object LessEqualThan : Operator //<=
        object Equal : Operator //==
        object NotEqual : Operator //!=

        object TernaryIf : Operator
        object TernaryElse : Operator
        object TernaryIfElse : Operator
    }

    data class FunctionCall(
        val argsCount: Int,
        val function: Function
    ) : Token {

        operator fun invoke(args: List<Any>) = function(*args.toTypedArray())

        object Delimiter: Token
    }

    sealed class Bracket : Token {
        object Left : Bracket()
        object Right : Bracket()
    }
}