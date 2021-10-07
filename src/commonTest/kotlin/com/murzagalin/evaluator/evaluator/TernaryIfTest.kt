package com.murzagalin.evaluator.evaluator

import com.murzagalin.evaluator.BooleanEvaluator
import com.murzagalin.evaluator.DoubleEvaluator
import com.murzagalin.evaluator.PreprocessedExpression
import com.murzagalin.evaluator.Token
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TernaryIfTest {
    private val doubleEvaluator = DoubleEvaluator()
    private val booleanEvaluator = BooleanEvaluator()

    @Test
    fun double_if_else() {
        assertEquals(
            1.0,
            doubleEvaluator.evaluate(
                PreprocessedExpression(
                    listOf(
                        Token.Operand.Boolean(true),
                        Token.Operand.Number(1),
                        Token.Operand.Number(2),
                        Token.Operator.TernaryIfElse
                    )
                )
            )
        )
        assertEquals(
            2.0,
            doubleEvaluator.evaluate(
                PreprocessedExpression(
                    listOf(
                        Token.Operand.Boolean(false),
                        Token.Operand.Number(1),
                        Token.Operand.Number(2),
                        Token.Operator.TernaryIfElse
                    )
                )
            )
        )
    }

    @Test
    fun boolean_if_else() {
        assertEquals(
            true,
            booleanEvaluator.evaluate(
                PreprocessedExpression(
                    listOf(
                        Token.Operand.Boolean(true),
                        Token.Operand.Boolean(true),
                        Token.Operand.Boolean(false),
                        Token.Operator.TernaryIfElse
                    )
                )
            )
        )
        assertEquals(
            false,
            booleanEvaluator.evaluate(
                PreprocessedExpression(
                    listOf(
                        Token.Operand.Boolean(false),
                        Token.Operand.Boolean(true),
                        Token.Operand.Boolean(false),
                        Token.Operator.TernaryIfElse
                    )
                )
            )
        )
    }


    @Test
    fun failed_if_else() {
        assertFailsWith<IllegalArgumentException> {
            booleanEvaluator.evaluate(
                PreprocessedExpression(
                    listOf(
                        Token.Operand.Boolean(true),
                        Token.Operand.Number(1),
                        Token.Operand.Boolean(false),
                        Token.Operator.TernaryIfElse
                    )
                )
            )
        }

        assertFailsWith<IllegalArgumentException> {
            booleanEvaluator.evaluate(
                PreprocessedExpression(
                    listOf(
                        Token.Operand.Number(1),
                        Token.Operand.Boolean(true),
                        Token.Operand.Boolean(false),
                        Token.Operator.TernaryIfElse
                    )
                )
            )
        }
    }
}