package integration

import evaluate
import kotlin.math.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FunctionsTests {

    @Test
    fun simple_functions_calls() {
        assertEquals(cos(1.0), "cos(1.0)".evaluate())
        assertEquals(ln(2.0), "ln(2.0)".evaluate())
        assertEquals(sin(1.0), "sin(1.0)".evaluate())
        assertEquals(tan(1.0), "tan(1.0)".evaluate())
        assertEquals(log(2.0, 3.0), "log(2.0, 3.0)".evaluate())
    }
}