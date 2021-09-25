package integration

import evaluateBoolean
import evaluateDouble
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TernaryIfTest {

    @Test
    fun simple_ternary_if() {
        assertEquals(true, "true?true:false".evaluateBoolean())
        assertEquals(false, "false?true:false".evaluateBoolean())
        assertEquals(true, "cond?true:false".evaluateBoolean(mapOf("cond" to true)))
        assertEquals(false, "cond?true:false".evaluateBoolean(mapOf("cond" to false)))
        assertEquals(true, "cond?op1:op2".evaluateBoolean(mapOf("cond" to true, "op1" to true, "op2" to false)))
        assertEquals(false, "cond?op1:op2".evaluateBoolean(mapOf("cond" to false, "op1" to true, "op2" to false)))

        assertEquals(1.0, "true?1:2".evaluateDouble())
        assertEquals(2.0, "false?1:2".evaluateDouble())
        assertEquals(1.0, "cond?1:2".evaluateDouble(mapOf("cond" to true)))
        assertEquals(2.0, "cond?1:2".evaluateDouble(mapOf("cond" to false)))
        assertEquals(1.0, "cond?op1:op2".evaluateDouble(mapOf("cond" to true, "op1" to 1.0, "op2" to 2.0)))
        assertEquals(2.0, "cond?op1:op2".evaluateDouble(mapOf("cond" to false, "op1" to 1.0, "op2" to 2.0)))
    }

    @Test
    fun ternary_if_in_ternary_if() {
        assertEquals(1.0, "true?1:false?2:3".evaluateDouble())
        assertEquals(3.0, "false?1:false?2:3".evaluateDouble())
        assertEquals(2.0, "false?1:true?2:3".evaluateDouble())

        assertEquals(1.0, "true?true?1:2:3".evaluateDouble())
        assertEquals(2.0, "true?false?1:2:3".evaluateDouble())
        assertEquals(3.0, "false?true?1:2:3".evaluateDouble())
    }

    @Test
    fun ternary_if_with_boolean_expression() {
        assertEquals(1.0, "true||false?1:2".evaluateDouble())
        assertEquals(2.0, "true&&false?1:2".evaluateDouble())
        assertEquals(1.0, "true&&!false?1:2".evaluateDouble())
    }

    @Test
    fun ternary_if_with_sum() {
        assertFails { "1+true?1:2".evaluateDouble() }
        assertFails { "1+false?1:2".evaluateDouble() }

        assertEquals(2.0, "1+(true?1:2)".evaluateDouble())
        assertEquals(3.0, "1+(false?1:2)".evaluateDouble())
    }
}
