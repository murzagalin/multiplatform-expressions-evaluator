import kotlin.jvm.JvmInline

sealed interface Token {

    sealed interface Operand : Token {
        @JvmInline
        value class NInteger(val value: Int): Operand
    }

    sealed interface Operator : Token {
        object Sum : Operator
        object Multiplication : Operator
    }

}