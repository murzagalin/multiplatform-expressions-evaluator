package integration

import evaluate
import kotlin.test.Test
import kotlin.test.assertEquals

class UnaryOperatorsIntegrationTests {

    @Test
    fun several_unary_minuses() {
        assertEquals(5.0, "--5".evaluate())
        assertEquals(5.0, "-(-5)".evaluate())
        assertEquals(-5.0, "--(-5)".evaluate())
        assertEquals(5.0, "+(-(-5))".evaluate())
        assertEquals(-5.0, "---5".evaluate())
    }

    @Test
    fun unary_minus_and_plus_with_number() {
        assertEquals(-5.0, "-5".evaluate())
        assertEquals(5.0, "5".evaluate())
    }

    @Test
    fun unary_minus_and_plus_before_brackets() {
        assertEquals(-(3+55).toDouble(), "-(3+55)".evaluate())
        assertEquals((3+55).toDouble(), "(3+55)".evaluate())
    }

    @Test
    fun unary_minus_and_plus_with_brackets() {
        assertEquals(4-(3+55).toDouble(), "4+-(3+55)".evaluate())
        assertEquals(4-(3+55).toDouble(), "4-+(3+55)".evaluate())
    }
}