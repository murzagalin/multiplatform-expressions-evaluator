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

    sealed class Operator(val priority: Int, val associativity: Associativity) : Token {
        object Plus : Operator(6, Associativity.LEFT)
        object Minus : Operator(6, Associativity.LEFT)
        object Multiplication : Operator(7, Associativity.LEFT)
        object Division : Operator(7, Associativity.LEFT)
        object Modulo : Operator(7, Associativity.LEFT)
        object Power : Operator(9, Associativity.RIGHT)
        object UnaryMinus : Operator(8, Associativity.RIGHT)
        object UnaryPlus : Operator(8, Associativity.RIGHT)

        object And : Operator(3, Associativity.LEFT)
        object Or : Operator(2, Associativity.LEFT)
        object Not : Operator(4, Associativity.LEFT)

        object GreaterThan : Operator(5, Associativity.LEFT) //>
        object GreaterEqualThan : Operator(5, Associativity.LEFT) //>=
        object LessThan : Operator(5, Associativity.LEFT) //<
        object LessEqualThan : Operator(5, Associativity.LEFT) //<=
        object Equal : Operator(5, Associativity.LEFT) //==
        object NotEqual : Operator(5, Associativity.LEFT) //!=

        object TernaryIf : Operator(1, Associativity.RIGHT)
        object TernaryElse : Operator(1, Associativity.LEFT)
        object TernaryIfElse : Operator(1, Associativity.RIGHT)
    }

    data class FunctionCall(
        val argsCount: Int,
        val function: Function
    ) : Token {

        operator fun invoke(args: List<Operand>): Operand {
            val functionArgs = mutableListOf<Any>()
            args.forEach {
                when (it) {
                    is Operand.Number -> functionArgs.add(it.value)
                    is Operand.Boolean -> functionArgs.add(it.value)
                    else -> error("operand type ${it::class.simpleName} is not supported in function calls")
                }
            }

            return when (val result = function(*functionArgs.toTypedArray())) {
                is Number -> Operand.Number(result.toDouble())
                is Boolean -> Operand.Boolean(result)
                else -> error("function return type ${result::class.simpleName} is not supported")
            }
        }

        object Delimiter: Token
    }

    sealed class Bracket : Token {
        object Left : Bracket()
        object Right : Bracket()
    }

    enum class Associativity { LEFT, RIGHT }
}