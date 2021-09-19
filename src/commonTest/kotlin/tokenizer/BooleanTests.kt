package tokenizer

import Tokenizer
import kotlin.test.Test
import kotlin.test.assertContentEquals

class BooleanTests {

    private val subject = Tokenizer()

    @Test
    fun boolean_operators() {
        assertContentEquals(
            listOf(
                Token.Operand.Variable("var1"),
                Token.Operator.And,
                Token.Operand.Variable("var2")
            ),
            subject.tokenize("var1&&var2")
        )
        assertContentEquals(
            listOf(
                Token.Operand.Variable("var1"),
                Token.Operator.Or,
                Token.Operand.Variable("var2")
            ),
            subject.tokenize("var1||var2")
        )
        assertContentEquals(
            listOf(
                Token.Operator.Not,
                Token.Operand.Variable("var1"),
                Token.Operator.Or,
                Token.Operator.Not,
                Token.Operand.Variable("var2")
            ),
            subject.tokenize("!var1||!var2")
        )
    }
}