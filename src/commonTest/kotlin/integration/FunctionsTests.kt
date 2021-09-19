package integration

import evaluateDouble
import kotlin.math.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FunctionsTests {

    @Test
    fun simple_functions_calls() {
        assertEquals(cos(1.0), "cos(1.0)".evaluateDouble())
        assertEquals(ln(2.0), "ln(2.0)".evaluateDouble())
        assertEquals(sin(1.0), "sin(1.0)".evaluateDouble())
        assertEquals(tan(1.0), "tan(1.0)".evaluateDouble())
        assertEquals(log(2.0, 3.0), "log(2.0, 3.0)".evaluateDouble())
    }

    @Test
    fun expressions_with_functions() {
        assertEquals(1 + cos(1.0*12 + 3), "1 + cos(1.0*12+3)".evaluateDouble())
        assertEquals(2 * ln(2.0.pow(3)), "2*ln(2.0^3)".evaluateDouble())
        assertEquals(4*sin(1.0*ln(12.0)), "4*sin(1.0*ln(12))".evaluateDouble())
        assertEquals(5*tan(1.0+99.0+234)+-1, "5*tan(1.0+99.0 +234)+-1".evaluateDouble())
    }

    @Test
    fun evaluated_arguments_in_multiarg_functions() {
        assertEquals(log(2*2.0,3.0), "log(2*2, 3)".evaluateDouble())
        assertEquals(6*log(2.0*12, 3.0.pow(4)), "6*log(2.0*12, 3.0^4)".evaluateDouble())
    }
}