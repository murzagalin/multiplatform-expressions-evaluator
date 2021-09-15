import kotlin.test.Test
import kotlin.test.assertContentEquals

class ConverterTest {

    private val subject = Converter()

    @Test
    fun convert_sum() {
        val expression = listOf(
            Token.Operand.NInteger(4),
            Token.Operator.Sum,
            Token.Operand.NInteger(3)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.NInteger(4),
                Token.Operand.NInteger(3),
                Token.Operator.Sum
            ),
            result
        )
    }

    @Test
    fun convert_mult() {
        val expression = listOf(
            Token.Operand.NInteger(5),
            Token.Operator.Mult,
            Token.Operand.NInteger(6)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.NInteger(5),
                Token.Operand.NInteger(6),
                Token.Operator.Mult
            ),
            result
        )
    }

    @Test
    fun convert_sub() {
        val expression = listOf(
            Token.Operand.NInteger(3),
            Token.Operator.Sub,
            Token.Operand.NInteger(1)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.NInteger(3),
                Token.Operand.NInteger(1),
                Token.Operator.Sub
            ),
            result
        )
    }

    @Test
    fun convert_div() {
        val expression = listOf(
            Token.Operand.NInteger(8),
            Token.Operator.Div,
            Token.Operand.NInteger(6)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.NInteger(8),
                Token.Operand.NInteger(6),
                Token.Operator.Div
            ),
            result
        )
    }

    @Test
    fun convert_sum_mult() {
        val expression = listOf(
            Token.Operand.NInteger(8),
            Token.Operator.Sum,
            Token.Operand.NInteger(6),
            Token.Operator.Mult,
            Token.Operand.NInteger(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.NInteger(8),
                Token.Operand.NInteger(6),
                Token.Operand.NInteger(4),
                Token.Operator.Mult,
                Token.Operator.Sum,
            ),
            result
        )
    }

    @Test
    fun convert_mult_sum() {
        val expression = listOf(
            Token.Operand.NInteger(8),
            Token.Operator.Mult,
            Token.Operand.NInteger(6),
            Token.Operator.Sum,
            Token.Operand.NInteger(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.NInteger(8),
                Token.Operand.NInteger(6),
                Token.Operator.Mult,
                Token.Operand.NInteger(4),
                Token.Operator.Sum,
            ),
            result
        )
    }

    @Test
    fun convert_sub_mult() {
        val expression = listOf(
            Token.Operand.NInteger(8),
            Token.Operator.Sub,
            Token.Operand.NInteger(6),
            Token.Operator.Mult,
            Token.Operand.NInteger(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.NInteger(8),
                Token.Operand.NInteger(6),
                Token.Operand.NInteger(4),
                Token.Operator.Mult,
                Token.Operator.Sub,
            ),
            result
        )
    }

    @Test
    fun convert_div_mult() {
        val expression = listOf(
            Token.Operand.NInteger(8),
            Token.Operator.Div,
            Token.Operand.NInteger(6),
            Token.Operator.Mult,
            Token.Operand.NInteger(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.NInteger(8),
                Token.Operand.NInteger(6),
                Token.Operator.Div,
                Token.Operand.NInteger(4),
                Token.Operator.Mult,
            ),
            result
        )
    }

    @Test
    fun convert_div_pow() {
        val expression = listOf(
            Token.Operand.NInteger(8),
            Token.Operator.Div,
            Token.Operand.NInteger(6),
            Token.Operator.Pow,
            Token.Operand.NInteger(4)
        )
        val result = subject.convert(expression)

        assertContentEquals(
            listOf(
                Token.Operand.NInteger(8),
                Token.Operand.NInteger(6),
                Token.Operand.NInteger(4),
                Token.Operator.Pow,
                Token.Operator.Div
            ),
            result
        )
    }
}