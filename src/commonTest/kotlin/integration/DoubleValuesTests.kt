package integration

import evaluate
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals

class DoubleValuesTests {

    @Test
    fun simple_value() {
        assertEquals(3214.235, "3214.235".evaluate())
    }

    @Test
    fun sum() {
        assertEquals(3214.235+23576.1245+375.23576+1756.135, "3214.235+23576.1245+375.23576+1756.135".evaluate())
    }

    @Test
    fun double_with_int() {
        assertEquals(23+0.123*(124/60.124), "23+0.123*(124/60.124)".evaluate())
    }

    @Test
    fun expressions() {
        assertEquals(3 * 23.toDouble().pow(3.2), "23^3.2*3".evaluate())
    }
}