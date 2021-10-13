package com.github.murzagalin.evaluator.integration

import com.github.murzagalin.evaluator.Evaluator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TernaryIfTest {
    private val evaluator = Evaluator()

    @Test
    fun simple_ternary_if() {
        assertEquals(true, evaluator.evaluateBoolean("true?true:false"))
        assertEquals(false, evaluator.evaluateBoolean("false?true:false"))
        assertEquals(true, evaluator.evaluateBoolean("cond?true:false", mapOf("cond" to true)))
        assertEquals(false, evaluator.evaluateBoolean("cond?true:false", mapOf("cond" to false)))
        assertEquals(true, evaluator.evaluateBoolean("cond?op1:op2", mapOf("cond" to true, "op1" to true, "op2" to false)))
        assertEquals(false, evaluator.evaluateBoolean("cond?op1:op2", mapOf("cond" to false, "op1" to true, "op2" to false)))

        assertEquals(1.0, evaluator.evaluateDouble("true?1:2"))
        assertEquals(2.0, evaluator.evaluateDouble("false?1:2"))
        assertEquals(1.0, evaluator.evaluateDouble("cond?1:2", mapOf("cond" to true)))
        assertEquals(2.0, evaluator.evaluateDouble("cond?1:2", mapOf("cond" to false)))
        assertEquals(1.0, evaluator.evaluateDouble("cond?op1:op2", mapOf("cond" to true, "op1" to 1.0, "op2" to 2.0)))
        assertEquals(2.0, evaluator.evaluateDouble("cond?op1:op2", mapOf("cond" to false, "op1" to 1.0, "op2" to 2.0)))
    }

    @Test
    fun ternary_if_in_ternary_if() {
        assertEquals(1.0, evaluator.evaluateDouble("true?1:false?2:3"))
        assertEquals(3.0, evaluator.evaluateDouble("false?1:false?2:3"))
        assertEquals(2.0, evaluator.evaluateDouble("false?1:true?2:3"))

        assertEquals(1.0, evaluator.evaluateDouble("true?true?1:2:3"))
        assertEquals(2.0, evaluator.evaluateDouble("true?false?1:2:3"))
        assertEquals(3.0, evaluator.evaluateDouble("false?true?1:2:3"))
    }

    @Test
    fun ternary_if_with_boolean_expression() {
        assertEquals(1.0, evaluator.evaluateDouble("true||false?1:2"))
        assertEquals(2.0, evaluator.evaluateDouble("true&&false?1:2"))
        assertEquals(1.0, evaluator.evaluateDouble("true&&!false?1:2"))
    }

    @Test
    fun ternary_if_with_sum() {
        assertFails { evaluator.evaluateDouble("1+true?1:2") }
        assertFails { evaluator.evaluateDouble("1+false?1:2") }

        assertEquals(2.0, evaluator.evaluateDouble("1+(true?1:2)"))
        assertEquals(3.0, evaluator.evaluateDouble("1+(false?1:2)"))
    }
}
