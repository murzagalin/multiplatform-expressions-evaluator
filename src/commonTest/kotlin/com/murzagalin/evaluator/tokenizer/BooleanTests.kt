package com.murzagalin.evaluator.tokenizer

import com.murzagalin.evaluator.Token
import com.murzagalin.evaluator.Tokenizer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class BooleanTests {

    private val subject = Tokenizer()

    @Test
    fun simple_boolean_constants() {
        assertContentEquals(
            listOf(Token.Operand.Boolean(true)),
            subject.tokenize("true")
        )
        assertContentEquals(
            listOf(Token.Operand.Boolean(false)),
            subject.tokenize("false")
        )
        assertContentEquals(
            listOf(
                Token.Operator.Not,
                Token.Operand.Boolean(false),
                Token.Operator.And,
                Token.Operand.Boolean(true)
            ),
            subject.tokenize("!false&&true")
        )
    }

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

    @Test
    fun comparison() {
        assertEquals(
            listOf(
                Token.Operand.Number(1),
                Token.Operator.LessThan,
                Token.Operand.Number(2)
            ),
            subject.tokenize("1<2")
        )
        assertEquals(
            listOf(
                Token.Operand.Number(1),
                Token.Operator.GreaterThan,
                Token.Operand.Number(2)
            ),
            subject.tokenize("1>2")
        )
        assertEquals(
            listOf(
                Token.Operand.Number(1),
                Token.Operator.LessEqualThan,
                Token.Operand.Number(2)
            ),
            subject.tokenize("1<=2")
        )
        assertEquals(
            listOf(
                Token.Operand.Number(1),
                Token.Operator.GreaterEqualThan,
                Token.Operand.Number(2)
            ),
            subject.tokenize("1>=2")
        )
        assertEquals(
            listOf(
                Token.Operand.Number(1),
                Token.Operator.Equal,
                Token.Operand.Number(2)
            ),
            subject.tokenize("1==2")
        )
        assertEquals(
            listOf(
                Token.Operand.Number(1),
                Token.Operator.NotEqual,
                Token.Operand.Number(2)
            ),
            subject.tokenize("1!=2")
        )
    }
}