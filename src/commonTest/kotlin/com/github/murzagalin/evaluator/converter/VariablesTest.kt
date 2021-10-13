package com.github.murzagalin.evaluator.converter

import com.github.murzagalin.evaluator.Converter
import com.github.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertContentEquals

class VariablesTest {

    private val subject = Converter()

    @Test
    fun sum_of_variable_and_number() {
        val expression = listOf(
            Token.Operand.Variable("v"),
            Token.Operator.Plus,
            Token.Operand.Number(3.0)
        )

        assertContentEquals(
            listOf(
                Token.Operand.Variable("v"),
                Token.Operand.Number(3.0),
                Token.Operator.Plus
            ),
            subject.convert(expression).expression
        )
    }

    @Test
    fun variables_in_exponentiation() {
        val expression = listOf(
            Token.Operand.Number(1.2),
            Token.Operator.Multiplication,
            Token.Operand.Variable("var2"),
            Token.Operator.Power,
            Token.Operand.Variable("var3")
        )

        assertContentEquals(
            listOf(
                Token.Operand.Number(1.2),
                Token.Operand.Variable("var2"),
                Token.Operand.Variable("var3"),
                Token.Operator.Power,
                Token.Operator.Multiplication
            ),
            subject.convert(expression).expression
        )
    }
}