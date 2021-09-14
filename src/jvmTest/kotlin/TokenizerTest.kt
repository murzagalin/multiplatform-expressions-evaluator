import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class TokenizerTest {

    private val subject = Tokenizer()

    @Test
    fun `sum of 2 operands`() {
        val result = subject.tokenize("3+4")
        assertEquals(3, result.size)
        assertEquals(Token.Operand.NInteger(3), result[0])
        assertEquals(Token.Operator.Sum, result[1])
        assertEquals(Token.Operand.NInteger(4), result[2])
    }

    @Test
    fun `sum of 2 operands with spaces`() {
        val result = subject.tokenize(" 3 + 4 ")
        assertEquals(3, result.size)
        assertEquals(Token.Operand.NInteger(3), result[0])
        assertEquals(Token.Operator.Sum, result[1])
        assertEquals(Token.Operand.NInteger(4), result[2])
    }

    @Test
    fun `multiplication of 2 operands`() {
        val result = subject.tokenize("5*2")
        assertEquals(3, result.size)
        assertEquals(Token.Operand.NInteger(5), result[0])
        assertEquals(Token.Operator.Multiplication, result[1])
        assertEquals(Token.Operand.NInteger(2), result[2])
    }

    @Test
    fun `sum and multiplication with 3 operands`() {
        val result = subject.tokenize("1 + 8*9")
        assertEquals(5, result.size)
        assertContentEquals(
            listOf(
                Token.Operand.NInteger(1),
                Token.Operator.Sum,
                Token.Operand.NInteger(8),
                Token.Operator.Multiplication,
                Token.Operand.NInteger(9)
            ),
            result
        )
    }
}