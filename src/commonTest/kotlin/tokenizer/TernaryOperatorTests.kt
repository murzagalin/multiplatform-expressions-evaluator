package tokenizer

import Tokenizer
import kotlin.test.Test
import kotlin.test.assertContentEquals

class TernaryOperatorTests {
    private val subject = Tokenizer()

    @Test
    fun if_else_tokens() {
        val expression = "a?b:c"
        assertContentEquals(
            listOf(
                Token.Operand.Variable("a"),
                Token.Operator.TernaryIf,
                Token.Operand.Variable("b"),
                Token.Operator.TernaryElse,
                Token.Operand.Variable("c")
            ),
            subject.tokenize(expression)
        )
    }
}