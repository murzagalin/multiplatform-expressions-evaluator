import kotlin.jvm.JvmInline

sealed interface Token {

    sealed interface Operand : Token {
        @JvmInline
        value class Num(val value: Double) : Operand {
            constructor(value: Int): this(value.toDouble())
        }

        @JvmInline
        value class Variable(val value: String) : Operand
    }

    sealed class Operator(val priority: Int, val associativity: Associativity) : Token {
        object Sum : Operator(1, Associativity.LEFT)
        object Sub : Operator(1, Associativity.LEFT)
        object Mult : Operator(2, Associativity.LEFT)
        object Div : Operator(2, Associativity.LEFT)
        object Pow : Operator(3, Associativity.RIGHT)
        object UnaryMinus : Operator(4, Associativity.RIGHT)
        object UnaryPlus : Operator(4, Associativity.RIGHT)

        object And : Operator(2, Associativity.LEFT)
        object Or : Operator(1, Associativity.LEFT)
        object Not: Operator(4, Associativity.LEFT)
    }

    sealed class Function : Token {
        object Cos : Function()
        object Sin : Function()
        object Tan : Function()
        object Ln : Function()
        object Log : Function()

        object Delimiter: Token
    }

    sealed class Bracket : Token {
        object Left : Bracket()
        object Right : Bracket()
    }

    enum class Associativity {
        LEFT, RIGHT
    }
}