package integration

import evaluate
import kotlin.math.*
import kotlin.test.Test
import kotlin.test.assertEquals

class VariablesTests {


    @Test
    fun expressions_with_functions_and_variables() {
        assertEquals(
            1 + cos(1.0*12 + 3),
            "1 + cos(x*12+y)".evaluate(
                mapOf("x" to 1.0, "y" to 3.0)
            )
        )
        assertEquals(
            2 * ln(2.0.pow(3)),
            "2*ln(2.0^x)".evaluate(
                mapOf("x" to 3.0)
            )
        )
        assertEquals(
            4* sin(2.0* ln(12.0)),
            "x*sin(y*ln(z))".evaluate(
                mapOf("x" to 4.0, "y" to 2.0, "z" to 12.0)
            )
        )
        assertEquals(
            5* tan(1.0+99.0+234) +-1,
            "var1*tan(1.0+var2 +234)+var3".evaluate(
                mapOf("var1" to 5.0, "var2" to 99.0, "var3" to -1.0)
            )
        )
        assertEquals(
            5* tan(1.0+99.0+234) +1,
            "var1*tan(1.0+var2 +234)+-var3".evaluate(
                mapOf("var1" to 5.0, "var2" to 99.0, "var3" to -1.0)
            )
        )
    }
}