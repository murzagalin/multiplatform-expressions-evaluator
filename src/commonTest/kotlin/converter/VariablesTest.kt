package converter

import Converter
import kotlin.test.Test
import kotlin.test.assertContentEquals

class VariablesTest {

    private val subject = Converter()

    @Test
    fun sum_of_variable_and_number() {
        val expression = listOf(
            Token.Variable("v"),
            Token.Operator.Sum,
            Token.Operand(3.0)
        )

        assertContentEquals(
            listOf(
                Token.Variable("v"),
                Token.Operand(3.0),
                Token.Operator.Sum
            ),
            subject.convert(expression)
        )
    }

    @Test
    fun variables_in_exponentiation() {
        val expression = listOf(
            Token.Operand(1.2),
            Token.Operator.Mult,
            Token.Variable("var2"),
            Token.Operator.Pow,
            Token.Variable("var3")
        )

        assertContentEquals(
            listOf(
                Token.Operand(1.2),
                Token.Variable("var2"),
                Token.Variable("var3"),
                Token.Operator.Pow,
                Token.Operator.Mult
            ),
            subject.convert(expression)
        )
    }
}