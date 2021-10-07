package com.murzagalin.evaluator.converter

import com.murzagalin.evaluator.Converter
import com.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertContentEquals

class VariablesTest {

    private val subject = Converter()

    @Test
    fun sum_of_variable_and_number() {
        val expression = listOf(
            Token.Operand.Variable("v"),
            Token.Operator.Sum,
            Token.Operand.Num(3.0)
        )

        assertContentEquals(
            listOf(
                Token.Operand.Variable("v"),
                Token.Operand.Num(3.0),
                Token.Operator.Sum
            ),
            subject.convert(expression).expression
        )
    }

    @Test
    fun variables_in_exponentiation() {
        val expression = listOf(
            Token.Operand.Num(1.2),
            Token.Operator.Mult,
            Token.Operand.Variable("var2"),
            Token.Operator.Pow,
            Token.Operand.Variable("var3")
        )

        assertContentEquals(
            listOf(
                Token.Operand.Num(1.2),
                Token.Operand.Variable("var2"),
                Token.Operand.Variable("var3"),
                Token.Operator.Pow,
                Token.Operator.Mult
            ),
            subject.convert(expression).expression
        )
    }
}