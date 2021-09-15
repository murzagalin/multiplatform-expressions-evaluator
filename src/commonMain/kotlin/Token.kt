import kotlin.jvm.JvmInline

sealed interface Token {

    sealed interface Operand : Token {
        @JvmInline
        value class NInteger(val value: Int): Operand
    }

    sealed class Operator(val priority: Int, val associativity: Associativity) : Token {
        object Sum : Operator(1, Associativity.LEFT)
        object Sub : Operator(1, Associativity.LEFT)
        object Mult : Operator(2, Associativity.LEFT)
        object Div : Operator(2, Associativity.LEFT)
        object Pow : Operator(3, Associativity.RIGHT)
    }

    enum class Associativity {
        LEFT, RIGHT
    }
}