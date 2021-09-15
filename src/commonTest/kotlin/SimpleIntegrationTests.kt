import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleIntegrationTests {

    @Test
    fun simple_sum() {
        val testsNum = 100
        val random = Random.Default

        for (testIx in 0 until testsNum) {
            val operandsCount = random.nextInt(5, 15)
            val expressionBuilder = StringBuilder()
            var result = 0
            for (operandIx in 0 until operandsCount) {
                val newOperand = random.nextInt(0, 99999)
                expressionBuilder.append(newOperand).append('+')
                result += newOperand
            }

            val expression = expressionBuilder.removeSuffix("+").toString()

            assertEquals(
                result.toDouble(),
                expression.evaluate(),
                "wrong result in expression $expression"
            )
        }
    }

    @Test
    fun simple_mult() {
        val testsNum = 100
        val random = Random.Default

        for (testIx in 0 until testsNum) {
            val operandsCount = random.nextInt(2, 6)
            val expressionBuilder = StringBuilder()
            var result = 1
            for (operandIx in 0 until operandsCount) {
                val newOperand = random.nextInt(0, 99)
                expressionBuilder.append(newOperand).append('*')
                result *= newOperand
            }

            val expression = expressionBuilder.removeSuffix("*").toString()

            assertEquals(
                result.toDouble(),
                expression.evaluate(),
                "wrong result in expression $expression"
            )
        }
    }

    @Test
    fun simple_division() {
        assertEquals(4.0, "8/2".evaluate())
        assertEquals(3.0, "12/4".evaluate())
        assertEquals(0.5, "8/16".evaluate())
        assertEquals(0.25, "8/16/2".evaluate())
        assertEquals(0.125, "8/16/2/2".evaluate())
    }

    @Test
    fun simple_subtraction() {
        assertEquals(6.0, "8-2".evaluate())
        assertEquals(8.0, "12-4".evaluate())
        assertEquals(-8.0, " 8 - 16 ".evaluate())
        assertEquals(-10.0, "8-16-2".evaluate())
        assertEquals(-12.0, "8-16-2-2".evaluate())
    }

    @Test
    fun simple_exponentiation() {
        assertEquals(64.0, "8^2".evaluate())
        assertEquals(1728.0, "12^3".evaluate())
        assertEquals(256.0, "2^8".evaluate())
        assertEquals(27.0, "3^3".evaluate())
        assertEquals(512.0, "8^3".evaluate())
    }
}