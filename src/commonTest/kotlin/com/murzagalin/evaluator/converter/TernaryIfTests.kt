package com.murzagalin.evaluator.converter

import com.murzagalin.evaluator.Converter
import com.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertContentEquals

class TernaryIfTests {
    private val subject = Converter()

    @Test
    fun convert_ternary_if() {
        val expression = listOf(
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("b"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("c")
        )

        assertContentEquals(
            listOf(
                Token.Operand.Variable("a"),
                Token.Operand.Variable("b"),
                Token.Operand.Variable("c"),
                Token.Operator.TernaryIfElse
            ),
            subject.convert(expression)
        )
    }

    @Test
    fun convert_with_ternary_if_with_sum_in_condition() {
        val expression = listOf(
            Token.Operand.Variable("z"),
            Token.Operator.Sum,
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("b"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("c")
        )

        assertContentEquals(
            listOf(
                Token.Operand.Variable("z"),
                Token.Operand.Variable("a"),
                Token.Operator.Sum,
                Token.Operand.Variable("b"),
                Token.Operand.Variable("c"),
                Token.Operator.TernaryIfElse
            ),
            subject.convert(expression)
        )
    }

    @Test
    fun convert_sum_with_ternary_if() {
        val expression = listOf(
            Token.Operand.Variable("z"),
            Token.Operator.Sum,
            Token.Bracket.Left,
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("b"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("c"),
            Token.Bracket.Right
        )

        assertContentEquals(
            listOf(
                Token.Operand.Variable("z"),
                Token.Operand.Variable("a"),
                Token.Operand.Variable("b"),
                Token.Operand.Variable("c"),
                Token.Operator.TernaryIfElse,
                Token.Operator.Sum
            ),
            subject.convert(expression)
        )
    }

    @Test
    fun ternary_if_with_ternary_if_1() { //a ? b : x ? y : z --> a b x y z ?: ?:
        val expression = listOf(
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("b"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("x"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("y"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("z")
        )

        assertContentEquals(
            listOf(
                Token.Operand.Variable("a"),
                Token.Operand.Variable("b"),
                Token.Operand.Variable("x"),
                Token.Operand.Variable("y"),
                Token.Operand.Variable("z"),
                Token.Operator.TernaryIfElse,
                Token.Operator.TernaryIfElse
            ),
            subject.convert(expression)
        )
    }

    @Test
    fun ternary_if_with_ternary_if_2() { // a ? x ? y : z : b  -->  a x y z ?: b ?:
        val expression = listOf(
            Token.Operand.Variable("a"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("x"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("y"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("z"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("b")
        )

        assertContentEquals(
            listOf(
                Token.Operand.Variable("a"),
                Token.Operand.Variable("x"),
                Token.Operand.Variable("y"),
                Token.Operand.Variable("z"),
                Token.Operator.TernaryIfElse,
                Token.Operand.Variable("b"),
                Token.Operator.TernaryIfElse
            ),
            subject.convert(expression)
        )
    }

    @Test
    fun ternary_if_with_ternary_if_3() { // (x ? y : z) ? a : b  -->  x y z ?: a b ?:
        val expression = listOf(
            Token.Bracket.Left,
            Token.Operand.Variable("x"),
            Token.Operator.TernaryIf,
            Token.Operand.Variable("y"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("z"),
            Token.Bracket.Right,
            Token.Operator.TernaryIf,
            Token.Operand.Variable("a"),
            Token.Operator.TernaryElse,
            Token.Operand.Variable("b")
        )

        assertContentEquals(
            listOf(
                Token.Operand.Variable("x"),
                Token.Operand.Variable("y"),
                Token.Operand.Variable("z"),
                Token.Operator.TernaryIfElse,
                Token.Operand.Variable("a"),
                Token.Operand.Variable("b"),
                Token.Operator.TernaryIfElse
            ),
            subject.convert(expression)
        )
    }

}