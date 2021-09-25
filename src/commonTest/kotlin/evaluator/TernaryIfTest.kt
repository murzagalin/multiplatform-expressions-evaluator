package evaluator

import kotlin.test.Test
import kotlin.test.assertEquals

class TernaryIfTest {
    private val doubleEvaluator = DoubleEvaluator()

    @Test
    fun simple_if_else() {
        assertEquals(
            1.0,
            doubleEvaluator.evaluate(
                listOf(
                    Token.Operand.Bool(true),
                    Token.Operand.Num(1),
                    Token.Operand.Num(2),
                    Token.Operator.TernaryIfElse
                )
            )
        )
        assertEquals(
            2.0,
            doubleEvaluator.evaluate(
                listOf(
                    Token.Operand.Bool(false),
                    Token.Operand.Num(1),
                    Token.Operand.Num(2),
                    Token.Operator.TernaryIfElse
                )
            )
        )
    }
}