package integration

import evaluate
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals

class BracketsIntegrationTests {

    @Test
    fun sum_in_brackets_multiplied() {
        assertEquals(((351 + 621) * 43).toDouble(), "(351+621)*43".evaluate())
        assertEquals((351 + 621 * 43).toDouble(), "351+621*43".evaluate())
    }

    @Test
    fun division_powered() {
        assertEquals((12 + 32).toDouble().pow(3), "(12+32)^3".evaluate())
        assertEquals(12 + 32.0.pow(3), "12+32^3".evaluate())
    }
}