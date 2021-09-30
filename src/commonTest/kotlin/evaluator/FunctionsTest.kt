package evaluator

import COS
import LOG
import kotlin.math.cos
import kotlin.math.log
import kotlin.test.Test
import kotlin.test.assertEquals

class FunctionsTest {
    private val doubleEvaluator = DoubleEvaluator()

    @Test
    fun one_argument_function() {
        val expression = listOf(
            Token.Operand.Num(10),
            COS
        )

        assertEquals(cos(10.0), doubleEvaluator.evaluate(expression))
    }

    @Test
    fun two_arguments_function() {
        val expression = listOf(
            Token.Operand.Num(10),
            Token.Operand.Num(2),
            LOG
        )

        assertEquals(log(10.0, 2.0), doubleEvaluator.evaluate(expression))
    }
}