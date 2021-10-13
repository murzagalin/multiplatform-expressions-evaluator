package com.github.murzagalin.evaluator.tokenizer

import com.github.murzagalin.evaluator.Token
import com.github.murzagalin.evaluator.Tokenizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DoubleValuesTokenizerTest {

    private val subject = Tokenizer()

    @Test
    fun simple_double_value() {
        val expression = "234.532"
        val result = subject.tokenize(expression)

        assertEquals(
            listOf(Token.Operand.Number(234.532)),
            result
        )
    }

    @Test
    fun wrong_double_value() {
        val expression = "234.532.35"
        assertFailsWith<IllegalArgumentException>("error parsing number $expression") {
            subject.tokenize(expression)
        }
    }
}