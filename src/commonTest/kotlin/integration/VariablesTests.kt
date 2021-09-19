package integration

import evaluateDouble
import kotlin.math.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class VariablesTests {


    @Test
    fun expressions_with_functions_and_variables() {
        assertEquals(
            1 + cos(1.0*12 + 3),
            "1 + cos(x*12+y)".evaluateDouble(
                mapOf("x" to 1.0, "y" to 3.0)
            )
        )
        assertEquals(
            2 * ln(2.0.pow(3)),
            "2*ln(2.0^x)".evaluateDouble(
                mapOf("x" to 3.0)
            )
        )
        assertEquals(
            4* sin(2.0* ln(12.0)),
            "x*sin(y*ln(z))".evaluateDouble(
                mapOf("x" to 4.0, "y" to 2.0, "z" to 12.0)
            )
        )
        assertEquals(
            5* tan(1.0+99.0+234) +-1,
            "var1*tan(1.0+var2 +234)+var3".evaluateDouble(
                mapOf("var1" to 5.0, "var2" to 99.0, "var3" to -1.0)
            )
        )
        assertEquals(
            5* tan(1.0+99.0+234) +1,
            "var1*tan(1.0+var2 +234)+-var3".evaluateDouble(
                mapOf("var1" to 5.0, "var2" to 99.0, "var3" to -1.0)
            )
        )
    }

    @Test
    fun fail_resolving_test() {
        assertFailsWith<IllegalArgumentException>("Could not resolve variable 'y'") {
            "1 + cos(x*12+y)".evaluateDouble(mapOf("x" to 1.0))
        }
    }
}